/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.*;
import spaceisnear.game.GameContext;
import spaceisnear.game.objects.GameObject;
import spaceisnear.game.messages.Message;

/**
 *
 * @author LPzhelud
 */
public abstract class Component {

    @Getter private ArrayList<ComponentState> states = new ArrayList<>();
    /**
     * Notice that it is actually GameContext. Upcasting is left for compability with server side.
     */
    @Getter(AccessLevel.PROTECTED) private Object context = null;

    public abstract void processMessage(Message message);

    public void setContext(Object context) {
	if (this.context == null) {
	    this.context = context;
	}
    }

    public static Component getInstance(ComponentStateBundle[] states, Class component) throws ClassNotFoundException {
	try {
	    Component newInstance = (Component) component.newInstance();
	    newInstance.states = new ArrayList<>();
	    for (int i = 0; i < states.length; i++) {
		ComponentState componentState = states[i].getState();
		newInstance.states.add(componentState);
	    }
	    return newInstance;
	} catch (InstantiationException | IllegalAccessException ex) {
	    Logger.getLogger(Component.class.getName()).log(Level.SEVERE, null, ex);
	}
	return null;
    }
}
