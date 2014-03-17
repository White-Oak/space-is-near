package spaceisnear.game.components.server;

import org.whiteoak.parsing.interpretating.ast.*;
import spaceisnear.game.components.Component;
import spaceisnear.server.ServerContext;
import spaceisnear.server.objects.ServerGameObject;

/**
 *
 * @author White Oak
 */
public class InteractionScriptProccessor extends ScriptProcessor {

    private final static Function[] f = {};

    public InteractionScriptProccessor(ServerContext context, Component currentRequester,
				       ServerGameObject currentInteractor) {
	super(context, currentRequester, f, new Constant[]{
	    new Constant("item", currentInteractor == null ? "null" : currentInteractor.getId() + ""),
	    new Constant("emulatedType", "interaction")});
    }
}
