package spaceisnear.server.scriptsv2;

import spaceisnear.game.messages.MessageInteracted;
import spaceisnear.server.ServerContext;
import spaceisnear.server.objects.items.StaticItem;

public abstract class ContextProcessorScript extends ItemScript {

    private int chosen;
    private int interactor;

    public void init(ServerContext context, StaticItem item, int chosen, int interactor) {
	super.init(context, item);
	this.chosen = chosen;
	this.interactor = interactor;
    }

    protected int getChosen() {
	return chosen;
    }

    protected void interact() {
	MessageInteracted messageInteracted = new MessageInteracted(getItem().getId(), interactor);
	context.sendDirectedMessage(messageInteracted);
    }
}
