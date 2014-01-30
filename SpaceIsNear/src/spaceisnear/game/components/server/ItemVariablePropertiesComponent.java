/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components.server;

import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentState;
import spaceisnear.game.components.ComponentType;
import spaceisnear.game.messages.Message;

/**
 *
 * @author White Oak
 */
public class ItemVariablePropertiesComponent extends Component {

    public ItemVariablePropertiesComponent() {
	super(ComponentType.ITEM_VARIABLES);
    }

    @Override
    public void processMessage(Message message) {
    }

    public void setProperty(String name, Object value) {
	addState(new ComponentState(name, value));
    }

    public Object getProperty(String name) {
	return getStateValueNamed(name);
    }

}
