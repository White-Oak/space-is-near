/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import spaceisnear.game.components.ComponentState;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import spaceisnear.game.bundles.ObjectBundle;

/**
 *
 * @author LPzhelud
 */
@AllArgsConstructor public class GameObjectState {

    @Getter private final ArrayList<ComponentState> states;
    @Getter private final int id;
    @Getter private final GameObjectType type;

    public ObjectBundle getBundle() {
	ComponentState[] states_a = states.toArray(new ComponentState[states.size()]);
	ObjectBundle objectBundle = new ObjectBundle();
	objectBundle.objectID = id;
	objectBundle.objectType = type.ordinal();
	objectBundle.states = states_a;
	return objectBundle;
    }
}
