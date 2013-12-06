/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import spaceisnear.game.messages.HurtMessage;
import spaceisnear.game.messages.Message;

/**
 *
 * @author White Oak
 */
public class HealthComponent extends Component {

    public static final int MAX_HEALTH = 100;
    public static final int MIN_HEALTH = 0;
    public static final int CRIT_HEALTH = 20;
    public static final int SICK_HEALTH = 80;

    /**
     *
     * @param owner
     */
    public HealthComponent() {
	super(ComponentType.HEALTH);
	addState(new ComponentState("health", 100));
    }

    @Override
    public void processMessage(Message message) {
	switch (message.getMessageType()) {
	    case HURT:
		HurtMessage hm = (HurtMessage) message;
		ComponentState health = getStateNamed("health");
		health.setValue(((Integer) health.getValue()) - hm.getDamage());
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

    public enum State {

	ALLRIGHT, DEAD, CRITICICAL, SICK
    }
}
