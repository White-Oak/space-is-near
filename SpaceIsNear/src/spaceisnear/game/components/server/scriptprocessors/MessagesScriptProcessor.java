package spaceisnear.game.components.server.scriptprocessors;

import lombok.Getter;
import org.whiteoak.parsing.interpretating.ast.*;
import spaceisnear.game.components.Component;
import spaceisnear.game.messages.Message;
import spaceisnear.server.ServerContext;

@Deprecated public class MessagesScriptProcessor extends ScriptProcessor {

    private final static Function[] f = {
	new NativeFunction("getPropertyMessageName"),
	new NativeFunction("getPropertyMessageValue"),
	new NativeFunction("getMessageType")};
    @Getter private final Message message;

    public MessagesScriptProcessor(ServerContext context, Component currentRequester, Message message) {
	super(context, currentRequester, f, null, 1);
	this.message = message;
    }

    @Override
    public String callNativeFunction(String name, Value[] values) {
	switch (name) {
	    case "getMessageType":
		return message.getMessageType().toString();
	}
	return super.callNativeFunction(name, values);
    }

}
