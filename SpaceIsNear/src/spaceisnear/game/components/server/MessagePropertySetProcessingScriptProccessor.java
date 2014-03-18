package spaceisnear.game.components.server;

import org.whiteoak.parsing.interpretating.ast.*;
import spaceisnear.abstracts.Context;
import spaceisnear.game.messages.properties.MessagePropertySet;
import spaceisnear.server.ServerContext;

/**
 *
 * @author White Oak
 */
public class MessagePropertySetProcessingScriptProccessor extends ScriptProcessor {

    private final MessagePropertySet currentMessage;
    private final static Function[] f = {
	new NativeFunction("getPropertyMessageName"),
	new NativeFunction("getPropertyMessageValue")};

    public MessagePropertySetProcessingScriptProccessor(ServerContext context, VariablePropertiesComponent currentRequester,
							MessagePropertySet currentMessage) {
	super(context, currentRequester, f, new Constant[]{new Constant("type", currentMessage.getMessageType().name())}, 1);
	this.currentMessage = currentMessage;
    }

    @Override
    public String callNativeFunction(String name, Value[] values) {
	super.callNativeFunction(name, values);
	switch (name) {
	    case "getPropertyMessageName":
		return currentMessage.getName();
	    case "getPropertyMessageValue":
		Context.LOG.log("Current message value is " + currentMessage.getValue());
		return (String) currentMessage.getValue();
	}
	return null;
    }
}
