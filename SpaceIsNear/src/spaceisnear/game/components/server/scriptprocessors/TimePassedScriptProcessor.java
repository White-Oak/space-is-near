package spaceisnear.game.components.server.scriptprocessors;

import org.whiteoak.parsing.interpretating.ast.Value;
import spaceisnear.game.components.Component;
import spaceisnear.server.ServerContext;

public class TimePassedScriptProcessor extends MessagesScriptProcessor {

    public TimePassedScriptProcessor(ServerContext context, Component currentRequester) {
	super(context, currentRequester, null);
    }

    @Override
    public String callNativeFunction(String name, Value[] values) {
	switch (name) {
	    case "getMessageType":
		return "TIME_PASSED";
	}
	return super.callNativeFunction(name, values);
    }
}
