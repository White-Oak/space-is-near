/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects;

import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.AllArgsConstructor;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentState;
import spaceisnear.game.components.ComponentStateBundle;

/**
 *
 * @author LPzhelud
 */
@AllArgsConstructor public class GameObjectState {

    private ComponentStateBundle[][] states;
    private String[] classes;

    GameObjectState() {
    }

    public Component[] getComponents() {
	Component[] components = new Component[classes.length];
	for (int i = 0; i < classes.length; i++) {
	    try {
		Class class1 = Class.forName(classes[i]);
		components[i] = Component.getInstance(states[i], class1);
	    } catch (ClassNotFoundException ex) {
		Logger.getLogger(GameObjectState.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
	return components;
    }
}
