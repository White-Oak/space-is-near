package spaceisnear.game.components.server.scriptprocessors.context;

import java.util.ArrayList;
import spaceisnear.game.messages.MessageActionsOffer;
import spaceisnear.server.ServerContext;
import spaceisnear.server.objects.items.StaticItem;

/**
 *
 * @author White Oak
 */
public class ContextMenuForItems {

    private static ServerContextMenu getMenu(ServerContext context, StaticItem[] items) {
	ArrayList<ServerContextSubMenu> subMenus = new ArrayList<>();
	for (int i = 0; i < items.length; i++) {
	    StaticItem staticItem = items[i];
	    final ContextSubMenuForItemsCreator subCreator = new ContextSubMenuForItemsCreator(staticItem, context);
	    subCreator.run();
	    ArrayList<String> subMenu = subCreator.getSubMenu();
	    subMenus.add(new ServerContextSubMenu(subMenu.toArray(new String[subMenu.size()]),
		    staticItem.getProperties().getId(), subCreator.getDefaults()));
	}
	return new ServerContextMenu(subMenus.toArray(new ServerContextSubMenu[subMenus.size()]));
    }

    public static MessageActionsOffer getMessage(ServerContext context, StaticItem[] items) {
	return new MessageActionsOffer(getMenu(context, items));
    }

}
