package spaceisnear.game.components.server.scriptprocessors.context;

import java.util.ArrayList;
import spaceisnear.server.ServerContext;
import spaceisnear.server.objects.items.StaticItem;

/**
 *
 * @author White Oak
 */
public class ContextMenuForItems {

    public static ServerContextMenu getMenu(ServerContext context, StaticItem[] items) {
	ArrayList<ServerContextSubMenu> subMenus = new ArrayList<>();
	for (int i = 0; i < items.length; i++) {
	    StaticItem staticItem = items[i];
	    ArrayList<String> subMenu = new ContextSubMenuForItemsCreator(staticItem, context).getSubMenu();
	    subMenus.add(new ServerContextSubMenu(subMenu.toArray(new String[subMenu.size()]),
		    staticItem.getProperties().getId()));
	}
	return new ServerContextMenu(subMenus.toArray(new ServerContextSubMenu[subMenus.size()]));
    }

}
