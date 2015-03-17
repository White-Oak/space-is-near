package spaceisnear.game.components.server.scriptprocessors;

import org.whiteoak.parsing.interpretating.ast.Value;
import spaceisnear.game.components.server.VariablePropertiesComponent;
import spaceisnear.game.messages.properties.MessagePropertySet;
import spaceisnear.server.ServerContext;

/**
 *
 * @author White Oak
 */
@Deprecated public class MessagePropertySetProcessingScriptProccessor extends MessagesScriptProcessor {

    private final MessagePropertySet currentMessage;

    public MessagePropertySetProcessingScriptProccessor(ServerContext context, VariablePropertiesComponent currentRequester,
							MessagePropertySet currentMessage) {
	super(context, currentRequester, currentMessage);
	this.currentMessage = currentMessage;
    }

    @Override
    public String callNativeFunction(String name, Value[] values) {
	switch (name) {
	    case "getPropertyMessageName":
		return currentMessage.getName();
	    case "getPropertyMessageValue":
//		Context.LOG.log("Current message value is " + currentMessage.getValue());
		//@working
		return (String) currentMessage.getValue();
	}
	return super.callNativeFunction(name, values);
    }

}
