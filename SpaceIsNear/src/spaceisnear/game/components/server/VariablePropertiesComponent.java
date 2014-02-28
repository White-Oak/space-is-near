package spaceisnear.game.components.server;

import org.whiteoak.parsing.interpretating.ExceptionHandler;
import org.whiteoak.parsing.interpretating.IAcceptable;
import org.whiteoak.parsing.interpretating.ast.*;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentType;
import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.properties.MessagePropertySet;
import spaceisnear.game.objects.Position;
import spaceisnear.game.ui.console.LogLevel;
import spaceisnear.game.ui.console.LogString;
import spaceisnear.server.*;
import spaceisnear.server.objects.items.StaticItem;

/**
 *
 * @author White Oak
 */
public class VariablePropertiesComponent extends Component implements IAcceptable, ExceptionHandler {

    private boolean dontProcess = false;
    private Message currentMessage;
    Function[] f = {
	new NativeFunction("getMessageType"),
	new NativeFunction("getPropertyMessageName"),
	new NativeFunction("dontProcessOnYourOwn"),
	new NativeFunction("getProperty", 1),
	new NativeFunction("getPropertyMessageValue"),
	new NativeFunction("setProperty", 2),
	new NativeFunction("logInConsole", 2)};
    Constant[] c = {};

    public VariablePropertiesComponent() {
	super(ComponentType.VARIABLES);
    }

    @Override
    public void processMessage(Message message) {
	switch (message.getMessageType()) {
	    case PROPERTY_SET:
		MessagePropertySet mps = (MessagePropertySet) message;
		dontProcess = false;
		if (!dontProcess) {
		    switch (mps.getName()) {
			case "pull":
			    if (((Integer) mps.getValue()) != -1) {
				Integer value = (Integer) mps.getValue();
				StaticItem get = (StaticItem) getContext().getObjects().get(value);
				//checked if unstucked
				Object property = get.getVariableProperties().getProperty("stucked");
				if (property != null && !(Boolean) property) {
				    //1-tile-range of pulling
				    Position positionToPull = get.getPosition();
				    Position position = getPosition();
				    if (Math.abs(positionToPull.getX() - position.getX()) <= 1 && Math.abs(
					    positionToPull.getY() - position.getY()) <= 1) {
					setProperty(mps.getName(), mps.getValue());
				    }
				}
			    } else {
				setProperty(mps.getName(), mps.getValue());
			    }
			    break;
		    }
		}
		break;
	}
    }

    public void setProperty(String name, Object value) {
	addState(name, value);
    }

    public Object getProperty(String name) {
	return getStateValueNamed(name);
    }

    @Override
    public String callNativeFunction(String name, Value[] values) {
	switch (name) {
	    case "getProperty":
		return getProperty(values[0].getValue()).toString();
	    case "dontProcessOnYourOwn":
		dontProcess = true;
		break;
	    case "setProperty":
		setProperty(values[0].getValue(), values[1].getValue());
		break;
	    case "getPropertyMessageName":
		return ((MessagePropertySet) currentMessage).getName();
	    case "getPropertyMessageValue":
		return (String) ((MessagePropertySet) currentMessage).getValue();
	    case "getMessageType":
		return (currentMessage).getMessageType().toString();
	    case "logInConsole":
		ServerContext context = (ServerContext) getContext();
		context.log(new LogString(values[0].getValue(), LogLevel.valueOf(values[1].getValue())));
		break;
	}
	return null;
    }

    @Override
    public void paused() {
    }

    @Override
    public void acceptException(Exception ex) {
    }

}
