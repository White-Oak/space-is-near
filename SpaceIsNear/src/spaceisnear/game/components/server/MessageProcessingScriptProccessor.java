package spaceisnear.game.components.server;

import org.whiteoak.parsing.interpretating.*;
import org.whiteoak.parsing.interpretating.ast.*;
import spaceisnear.abstracts.Context;
import spaceisnear.game.messages.MessageLog;
import spaceisnear.game.messages.MessageToSend;
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
public class MessageProcessingScriptProccessor implements IAcceptable, ExceptionHandler {

    private final VariablePropertiesComponent currentRequester;
    private final MessagePropertySet currentMessage;
    private final ServerContext context;
    private final Interpretator interpretator;
    private final static Function[] f = {
	new NativeFunction("getPropertyMessageName"),
	new NativeFunction("dontProcessOnYourOwn"),
	new NativeFunction("getProperty", 1),
	new NativeFunction("getPropertyMessageValue"),
	new NativeFunction("setProperty", 2),
	new NativeFunction("sendPlayerPrivateMessage", 1),
	new NativeFunction("addProperty", 2)};
    private final Constant[] c;

    public MessageProcessingScriptProccessor(ServerContext context, VariablePropertiesComponent currentRequester,
					     MessagePropertySet currentMessage) {
	this.context = context;
	this.currentRequester = currentRequester;
	this.currentMessage = currentMessage;
	StaticItem owner = (StaticItem) currentRequester.getOwner();
	int id = owner.getProperties().getId();
	c = new Constant[]{new Constant("type", currentMessage.getMessageType().name()),
			   new Constant("emulatedType", "processingMessage")};
	Context.LOG.log("getting interpretator");
	interpretator = ServerItemsArchive.ITEMS_ARCHIVE.getInterprator(id, c, f, this);
    }

    public void run() {
	if (interpretator != null) {
	    Context.LOG.log("Running this shitty script");
	    interpretator.run(this, false);
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
	    case "setProperty":
		setProperty(values[0].getValue(), values[1].getValue());
		break;
	    case "getPropertyMessageName":
		return currentMessage.getName();
	    case "getPropertyMessageValue":
		Context.LOG.log("Current message value is " + currentMessage.getValue());
		return (String) currentMessage.getValue();
	    case "sendPlayerPrivateMessage":
		int ownerId = ((StaticItem) currentRequester.getOwner()).getPlayerId();
		Context.LOG.log("Sending private message to " + ownerId);
		LogString logString = new LogString(values[0].getValue(), LogLevel.PRIVATE, ownerId);
		MessageToSend messageToSend = new MessageToSend(new MessageLog(logString));
		context.sendDirectedMessage(messageToSend);
		break;
	    case "addProperty":
		Object property = getProperty(values[0].getValue());
		if (property == null) {
		    property = "";
		}
		setProperty(values[0].getValue(), property.toString() + values[1].getValue());
		break;
	}
	return null;
    }

    public void setProperty(String name, Object value) {
	currentRequester.setProperty(name, value);
    }

    public Object getProperty(String name) {
	return currentRequester.getProperty(name);
    }

    @Override
    public void paused() {
    }

    @Override
    public void acceptException(Exception ex) {
	ex.printStackTrace();
    }
}
