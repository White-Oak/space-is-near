package spaceisnear.game.messages;

import spaceisnear.abstracts.Context;
import spaceisnear.game.components.server.scriptprocessors.context.ContextMenuForItems;
import spaceisnear.server.*;
import spaceisnear.server.objects.items.StaticItem;

/**
 *
 * @author White Oak
 */
public class MessageActionsRequest extends Message implements NetworkableMessage {

    private final int ids[];

    public MessageActionsRequest(int[] ids) {
	super(MessageType.CONTEXT_ACTIONS_REQUEST);
	this.ids = ids;
    }

    @Override
    public void processForServer(ServerContext context, Client client) {
	StaticItem items[] = new StaticItem[ids.length];
	for (int i = 0; i < ids.length; i++) {
	    int j = ids[i];
	    assert j > 0 : "id of an item cannot be less than zero!";
	    StaticItem item = (StaticItem) context.getObjects().get(j);
	    if (item == null) {
		Exception ex = new NullPointerException("Object " + j + " no longer exists or has been called due to wrong usage.");
		Context.LOG.log(ex);
		return;
	    }
	    items[i] = item;
	}
	//
	//
	MessageActionsOffer message = ContextMenuForItems.getMessage(context, items);
	client.getPlayer().setMenu(message.getMenu());
	MessageToSend messageToSend = new MessageToSend(message);
	context.sendDirectedMessage(messageToSend);
    }

}
