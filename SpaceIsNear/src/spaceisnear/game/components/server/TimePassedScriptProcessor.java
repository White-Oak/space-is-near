package spaceisnear.game.components.server;

import org.whiteoak.parsing.interpretating.ast.Constant;
import spaceisnear.game.components.Component;
import spaceisnear.server.ServerContext;

public class TimePassedScriptProcessor extends ScriptProcessor {

    public TimePassedScriptProcessor(ServerContext context, Component currentRequester) {
	super(context, currentRequester, null,
		new Constant[]{new Constant("type", "TIME_PASSED")}, 1);
    }

}
