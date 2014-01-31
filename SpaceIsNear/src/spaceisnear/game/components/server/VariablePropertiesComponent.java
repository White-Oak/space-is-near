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
import spaceisnear.game.components.ComponentState;
import spaceisnear.game.components.ComponentType;
import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessagePropertySet;

/**
 *
 * @author White Oak
 */
public class VariablePropertiesComponent extends Component {

    public VariablePropertiesComponent() {
	super(ComponentType.VARIABLES);
    }

    @Override
    public void processMessage(Message message) {
	switch (message.getMessageType()) {
	    case PROPERTY_SET:
		MessagePropertySet mps = (MessagePropertySet) message;
		setProperty(mps.getName(), mps.getValue());
		break;
	}
    }

    public void setProperty(String name, Object value) {
	addState(new ComponentState(name, value));
    }

    public Object getProperty(String name) {
	return getStateValueNamed(name);
    }

}
