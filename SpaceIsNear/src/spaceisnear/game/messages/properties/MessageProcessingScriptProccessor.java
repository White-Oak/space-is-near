package spaceisnear.game.messages.properties;

import org.whiteoak.parsing.interpretating.ExceptionHandler;
import org.whiteoak.parsing.interpretating.IAcceptable;
import org.whiteoak.parsing.interpretating.ast.*;
import spaceisnear.game.components.server.VariablePropertiesComponent;
import spaceisnear.game.ui.console.LogLevel;
import spaceisnear.game.ui.console.LogString;
import spaceisnear.server.ServerContext;

/**
 *
 * @author White Oak
 */
public class MessageProcessingScriptProccessor implements IAcceptable, ExceptionHandler {

    private final VariablePropertiesComponent currentRequester;
    private final MessagePropertySet currentMessage;
    private final ServerContext context;
    final static Function[] f = {
	new NativeFunction("getMessageType"),
	new NativeFunction("getPropertyMessageName"),
	new NativeFunction("dontProcessOnYourOwn"),
	new NativeFunction("getProperty", 1),
	new NativeFunction("getPropertyMessageValue"),
	new NativeFunction("setProperty", 2),
	new NativeFunction("logInConsole", 2)};
    final static Constant[] c = {};

    public MessageProcessingScriptProccessor(VariablePropertiesComponent currentRequester, MessagePropertySet currentMessage,
					     ServerContext context) {
	this.currentRequester = currentRequester;
	this.currentMessage = currentMessage;
	this.context = context;
    }

    @Override
    public String callNativeFunction(String name, Value[] values) {
	switch (name) {
	    case "getProperty":
		return getProperty(values[0].getValue()).toString();
	    case "dontProcessOnYourOwn":
		currentRequester.setDontProcess(true);
		break;
	    case "setProperty":
		setProperty(values[0].getValue(), values[1].getValue());
		break;
	    case "getPropertyMessageName":
		return currentMessage.getName();
	    case "getPropertyMessageValue":
		return (String) currentMessage.getValue();
	    case "getMessageType":
		return currentMessage.getMessageType().toString();
	    case "logInConsole":
		context.log(new LogString(values[0].getValue(), LogLevel.valueOf(values[1].getValue())));
		break;
	}
	return null;
    }

    public void setProperty(String name, Object value) {
	currentRequester.setProperty(name, value);
    }

    public Object getProperty(String name) {
	return currentRequester.getProperty(name);
    }

    @Override
    public void paused() {
    }

    @Override
    public void acceptException(Exception ex) {
    }
}
