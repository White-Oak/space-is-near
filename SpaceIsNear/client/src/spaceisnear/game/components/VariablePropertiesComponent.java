package spaceisnear.game.components;

import spaceisnear.game.components.*;
import spaceisnear.game.messages.Message;

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
    }

    public void setProperty(String name, Object value) {
	addState(name, value);
    }

    public Object getProperty(String name) {
	return getStateValueNamed(name);
    }
}
