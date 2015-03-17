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

import spaceisnear.game.components.*;
import spaceisnear.game.messages.*;
import spaceisnear.game.ui.console.*;
import spaceisnear.server.ServerContext;
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
    private static final int KNOCKBACKED_PASSES = 200;
    private static final int FLOOD_DELTA = 150;
    private static final int FLOOD_DELTA_FOR_KNOCKBACKING = 1500;
    private boolean ableToFlood = true, ableToFloodKnockbacking = true;

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
		HurtMessage hurtMessage = (HurtMessage) message;
		changeHealth(hurtMessage.getDamage());
		final Player player = (Player) getOwner();
		switch (hurtMessage.getType()) {
		    case SUFFOCATING:
			if (ableToFlood) {
			    final ServerContext context = (ServerContext) getContext();
			    final String nickname = player.getNickname();
			    context.chatLog(new LogString(nickname + " задыхается.", LogLevel.TALKING, getPosition()));
			    ableToFlood = false;
			    registerForOneShotTask(() -> ableToFlood = true, FLOOD_DELTA);
			}
			break;
		}
		final VariablePropertiesComponent variablePropertiesComponent;
		variablePropertiesComponent = getOwner().getVariablePropertiesComponent();
		//
		switch (getState()) {
		    case CRITICICAL:
			if (ableToFloodKnockbacking) {
			    MessageKnockbacked messageKnockbacked = new MessageKnockbacked(player.getId());
			    variablePropertiesComponent.setProperty("knockbacked", true);
			    registerForOneShotTask(() -> variablePropertiesComponent.setProperty("knockbacked", false),
				    KNOCKBACKED_PASSES);
			    final ServerContext context = (ServerContext) getContext();
			    final String nickname = player.getNickname();
			    context.chatLog(new LogString(nickname + " упал без сознания.", LogLevel.TALKING, getPosition()));
			    //I suppose it should not be knockbacking but animation sending
			    getContext().sendToID(new MessageToSend(messageKnockbacked), player.getId());
			    ableToFloodKnockbacking = false;
			    registerForOneShotTask(() -> ableToFloodKnockbacking = true, FLOOD_DELTA_FOR_KNOCKBACKING);
			}
			break;

		    case DEAD:
			MessageDied messageDied = new MessageDied(player.getId());
			variablePropertiesComponent.setProperty("dead", true);
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
	//Context.LOG.log(getHealth());
    }

    public int getHealth() {
	return (int) getStateValueNamed("health");
    }

    public enum State {

	ALLRIGHT, DEAD, CRITICICAL, SICK
    }
}
