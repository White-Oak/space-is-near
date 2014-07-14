package spaceisnear.game.components.server.scriptprocessors;

import org.apache.commons.lang3.ArrayUtils;
import org.whiteoak.parsing.interpretating.*;
import org.whiteoak.parsing.interpretating.ast.*;
import spaceisnear.abstracts.Context;
import spaceisnear.game.components.Component;
import spaceisnear.game.messages.*;
import spaceisnear.game.messages.properties.MessagePropertySet;
import spaceisnear.game.ui.console.LogLevel;
import spaceisnear.game.ui.console.LogString;
import spaceisnear.server.ServerContext;
import spaceisnear.server.objects.items.ServerItemsArchive;
import spaceisnear.server.objects.items.StaticItem;

/**
 *
 * @author White Oak
 */
@Deprecated public abstract class ScriptProcessor implements IAcceptable, ExceptionHandler {

    private final static Function[] fs = {
	new NativeFunction("dontProcessOnYourOwn"),
	new NativeFunction("getProperty", 1),
	new NativeFunction("setProperty", 2),
	new NativeFunction("setPropertyAndSend", 2),
	new NativeFunction("sendPlayerPrivateMessage", 1),
	new NativeFunction("concatenateProperty", 2),
	new NativeFunction("sendAnimationQueueToRequestor", 2),
	new NativeFunction("registerForTimeMessage", 1),
	new NativeFunction("setFullyPathable", 1)};
    private final Interpretator interpretator;
    private final ServerContext context;
    private final Component currentRequester;

    public ScriptProcessor(ServerContext context, Component currentRequester, Function[] f, Constant[] c, int mode) {
	if (f != null) {
	    f = ArrayUtils.addAll(fs, f);
	} else {
	    f = fs;
	}
	this.context = context;
	//Context.LOG.log("getting interpretator");
	this.currentRequester = currentRequester;
	StaticItem item = (StaticItem) currentRequester.getOwner();
	interpretator = ServerItemsArchive.ITEMS_ARCHIVE.getInterprator(item.getProperties().getId(), c, f, this, mode);
    }

    public final void run() {
	if (interpretator != null) {
	    //Context.LOG.log("Running this shitty script");
	    interpretator.run(this, false);
	} else {
	    Context.LOG.log("Theres nothing to run!");
	}
    }

    @Override
    public String callNativeFunction(String name, Value[] values) {
	switch (name) {
	    case "getProperty":
		return getProperty(values[0].getValue()).toString();
	    case "dontProcessOnYourOwn":
		currentRequester.setDontProcess(true);
		break;
	    case "setPropertyAndSend": {
		final MessagePropertySet messagePropertySet;
		messagePropertySet = new MessagePropertySet(currentRequester.getOwnerId(),
			values[0].getValue(), values[1].getValue());
		MessageToSend messageToSend = new MessageToSend(messagePropertySet);
		context.sendDirectedMessage(messageToSend);
	    }
	    case "setProperty":
		setProperty(values[0].getValue(), values[1].getValue());
		break;
	    case "sendPlayerPrivateMessage": {
		int ownerId = ((StaticItem) currentRequester.getOwner()).getPlayerId();
		Context.LOG.log("Sending private message to " + ownerId);
		LogString logString = new LogString(values[0].getValue(), LogLevel.PRIVATE, ownerId);
		MessageToSend messageToSend = new MessageToSend(new MessageLog(logString));
		context.sendDirectedMessage(messageToSend);
	    }
	    break;
	    case "concatenateProperty":
		Object property = getProperty(values[0].getValue());
		if (property == null) {
		    property = "";
		}
		setProperty(values[0].getValue(), property.toString() + values[1].getValue());
		break;
	    case "sendAnimationQueueToRequestor":
		String imageIdsS[] = values[0].getValue().split(", ");
		int imageIds[] = new int[imageIdsS.length];
		for (int i = 0; i < imageIdsS.length; i++) {
		    String object = imageIdsS[i];
		    imageIds[i] = Integer.parseInt(object);
		}
		int animationDelta = Integer.parseInt(values[1].getValue());
		MessageAnimationChanged messageAnimationChanged;
		messageAnimationChanged = new MessageAnimationChanged(currentRequester.getOwnerId(), imageIds, animationDelta);
		MessageToSend messageToSend = new MessageToSend(messageAnimationChanged);
		context.sendDirectedMessage(messageToSend);
		break;
	    case "registerForTimeMessage":
		TimePassedScriptProcessor timePassedScriptProcessor = new TimePassedScriptProcessor(context, currentRequester);
		currentRequester.registerForOneShotTask(() -> timePassedScriptProcessor.run(),
			Integer.parseInt(values[0].getValue()));
		break;
	    case "setFullyPathable":
		boolean set = Boolean.parseBoolean(values[0].getValue());
		setProperty("blockingPath", String.valueOf(!set));
		setProperty("blockingAir", String.valueOf(!set));
		ServerContext context1 = context;
		context1.getObstacles().setReacheable(currentRequester.getPosition().getX(), currentRequester.getPosition().getY(), set);
		context1.getAtmosphere().setAirReacheable(currentRequester.getPosition().getX(), currentRequester.getPosition().getY(), set);
		break;
	}
	return null;
    }

    public void setProperty(String name, Object value) {
	currentRequester.getOwner().getVariablePropertiesComponent().setProperty(name, value);
    }

    public Object getProperty(String name) {
	final Object property = currentRequester.getOwner().getVariablePropertiesComponent().getProperty(name);
	if (property == null) {
	    return "null";
	}
	return property;
    }

    @Override
    public void paused() {
    }

    @Override
    public void acceptException(Exception ex) {
	ex.printStackTrace();
    }
}
