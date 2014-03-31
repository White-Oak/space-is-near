package spaceisnear.game.components.server.scriptprocessors;

import spaceisnear.game.components.Component;
import spaceisnear.server.ServerContext;

public class TimePassedScriptProcessor extends ScriptProcessor {

    public TimePassedScriptProcessor(ServerContext context, Component currentRequester) {
	super(context, currentRequester, null, null, 2);
    }
}
