package spaceisnear.game.components.server.scriptprocessors;

import org.whiteoak.parsing.interpretating.ast.*;
import spaceisnear.game.components.Component;
import spaceisnear.server.ServerContext;

/**
 *
 * @author White Oak
 */
public class InteractionScriptProccessor extends ScriptProcessor {

    private final static Function[] f = {new NativeFunction("getItem")};
    private final int currentInteractor;

    public InteractionScriptProccessor(ServerContext context, Component currentRequester,
				       int currentInteractor) {
	super(context, currentRequester, f, null, 0);
	this.currentInteractor = currentInteractor;
    }

    @Override
    public String callNativeFunction(String name, Value[] values) {
	switch (name) {
	    case "getItem":
		return String.valueOf(currentInteractor);
	}
	return super.callNativeFunction(name, values); //To change body of generated methods, choose Tools | Templates.
    }

}
