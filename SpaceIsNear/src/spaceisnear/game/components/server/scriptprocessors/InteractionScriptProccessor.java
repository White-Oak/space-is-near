package spaceisnear.game.components.server.scriptprocessors;

import org.whiteoak.parsing.interpretating.ast.*;
import spaceisnear.game.components.Component;
import spaceisnear.server.ServerContext;
import spaceisnear.server.objects.items.StaticItem;

/**
 *
 * @author White Oak
 */
public class InteractionScriptProccessor extends ScriptProcessor {

    private final static Function[] f = {new NativeFunction("getItem")};
    private final StaticItem currentInteractor;

    public InteractionScriptProccessor(ServerContext context, Component currentRequester,
				       StaticItem currentInteractor) {
	super(context, currentRequester, f, null, 0);
	this.currentInteractor = currentInteractor;
    }

    @Override
    public String callNativeFunction(String name, Value[] values) {
	switch (name) {
	    case "getItem":
		return (String) (currentInteractor == null ? "null" : currentInteractor.getId());
	}
	return super.callNativeFunction(name, values); //To change body of generated methods, choose Tools | Templates.
    }

}
