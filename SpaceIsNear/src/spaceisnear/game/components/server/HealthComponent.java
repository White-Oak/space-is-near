/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components.server;

import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentType;
import spaceisnear.game.messages.HurtMessage;
import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessageDied;
import spaceisnear.game.messages.MessageKnockbacked;
import spaceisnear.game.messages.MessageToSend;
import spaceisnear.game.ui.console.LogLevel;
import spaceisnear.game.ui.console.LogString;
import spaceisnear.server.ServerContext;
import spaceisnear.server.ServerNetworking;
import spaceisnear.server.objects.Player;

/**
 *
 * @author White Oak
 */
public class HealthComponent extends Component {

    public static final int MAX_HEALTH = 100;
    public static final int MIN_HEALTH = 0;
    public static final int CRIT_HEALTH = 20;
    public static final int SICK_HEALTH = 80;
    public static final int SUFFOCATING_DAMAGE = -10;
    public static final int LIGHT_SUFFOCATING_DAMAGE = -1;

    /**
     *
     * @param owner
     */
    public HealthComponent() {
	super(ComponentType.HEALTH);
	addState("health", 100);
    }

    @Override
    public void processMessage(Message message) {
	switch (message.getMessageType()) {
	    case HURT:
		HurtMessage hm = (HurtMessage) message;
		changeHealth(hm.getDamage());
		final ServerContext context = (ServerContext) getContext();
		final Player player = (Player) getOwner();
		switch (hm.getType()) {
		    case SUFFOCATING:
			final String nickname = player.getNickname();
			final ServerNetworking networking = context.getNetworking();
			networking.log(new LogString(nickname + " задыхается.", LogLevel.TALKING, getPosition()));
			break;
		}
		//
		switch (getState()) {
		    case CRITICICAL:
			MessageKnockbacked messageKnockbacked = new MessageKnockbacked(player.getId());
//			getContext().sendToID(messageKnockbacked, player.getId());
			getOwner().getVariablePropertiesComponent().setProperty("knockbacked", true);
			getContext().sendToID(new MessageToSend(messageKnockbacked), player.getId());
			break;

		    case DEAD:
			MessageDied messageDied = new MessageDied(player.getId());
			getOwner().getVariablePropertiesComponent().setProperty("dead", true);
			getContext().sendToID(new MessageToSend(messageDied), player.getId());
			break;

		}
		break;
	}
    }

    /**
     *
     * @return state of a living creature.
     */
    public State getState() {
	int health = (Integer) getStateValueNamed("health");
	if (health > SICK_HEALTH) {
	    return State.ALLRIGHT;
	} else if (health > CRIT_HEALTH) {
	    return State.SICK;
	} else if (health > MIN_HEALTH) {
	    return State.CRITICICAL;
	} else {
	    return State.DEAD;
	}
    }

    public void changeHealth(int delta) {
	setStateValueNamed("health", getHealth() + delta);
	System.out.println(getHealth());
    }

    public int getHealth() {
	return (int) getStateValueNamed("health");
    }

    public enum State {

	ALLRIGHT, DEAD, CRITICICAL, SICK
    }
}
