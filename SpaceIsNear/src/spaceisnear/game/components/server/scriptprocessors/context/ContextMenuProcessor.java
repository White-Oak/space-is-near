package spaceisnear.game.components.server.scriptprocessors.context;

import org.whiteoak.parsing.interpretating.*;
import org.whiteoak.parsing.interpretating.ast.*;
import spaceisnear.game.messages.MessageInteracted;
import spaceisnear.game.ui.ActivationListener;
import spaceisnear.game.ui.UIElement;
import spaceisnear.game.ui.context.ContextMenu;
import spaceisnear.server.ServerContext;
import spaceisnear.server.objects.items.ServerItemsArchive;
import spaceisnear.server.objects.items.StaticItem;

/**
 *
 * @author White Oak
 */
public class ContextMenuProcessor implements IAcceptable, ExceptionHandler, ActivationListener {

    private final static Function[] fs = {
	new NativeFunction("interact"),
	new NativeFunction("getChosen", 1)};
    private final Interpretator interpretator;
    private int chosen;
    private final StaticItem item;
    private final ServerContext context;

    public ContextMenuProcessor(StaticItem item, ServerContext context) {
	interpretator = ServerItemsArchive.ITEMS_ARCHIVE.getInterprator(item.getProperties().getId(), new Constant[]{}, fs,
		this, 4);
	this.item = item;
	this.context = context;
    }

    public void run(int chosen) {
	this.chosen = chosen;
	interpretator.run(this, true);
    }

    @Override
    public String callNativeFunction(String name, Value[] values) {
	switch (name) {
	    case "getChosen":
		return String.valueOf(chosen);
	    case "interact":
		MessageInteracted messageInteracted = new MessageInteracted(item.getId(), -1);
		context.sendDirectedMessage(messageInteracted);
		break;
	}
	return null;
    }

    @Override
    public void paused() {
    }

    @Override
    public void acceptException(Exception excptn) {
    }

    @Override
    public void componentActivated(UIElement actor) {
	ContextMenu menu = (ContextMenu) actor;
	chosen = menu.getSelected();
	interpretator.run(this, false);
    }

}
