/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author White Oak
 */
public class ComponentStateBundle {

    private String name;
    private String value;
    private String className;

    public ComponentStateBundle(ComponentState state) {
	name = state.getName();
	value = new Gson().toJson(state.getValue());
	className = state.getValue().getClass().getName();
    }

    public ComponentState getState() throws ClassNotFoundException {
	return new ComponentState(name, new Gson().fromJson(value, Class.forName(className)));
    }
}
