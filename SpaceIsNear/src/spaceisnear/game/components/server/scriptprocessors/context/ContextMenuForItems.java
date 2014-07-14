package spaceisnear.game.components.server.scriptprocessors.context;

import java.util.ArrayList;
import spaceisnear.game.messages.MessageActionsOffer;
import spaceisnear.server.ServerContext;
import spaceisnear.server.objects.items.StaticItem;
import spaceisnear.server.scriptsv2.ContextRequestScript;
import spaceisnear.server.scriptsv2.ScriptsManager;

/**
 *
 * @author White Oak
 */
public class ContextMenuForItems {

    private static ServerContextMenu getMenu(ServerContext context, StaticItem[] items) {
	ArrayList<ServerContextSubMenu> subMenus = new ArrayList<>();
	for (int i = 0; i < items.length; i++) {
	    StaticItem staticItem = items[i];
	    final ContextRequestScript requestScript = (ContextRequestScript) context.getScriptsManager().getScriptFor(
		    ScriptsManager.ScriptType.CONREQ, staticItem.getProperties().getName());
	    if (requestScript != null) {
		requestScript.init(context, staticItem);
		requestScript.script();
		ArrayList<String> subMenu = requestScript.getSubMenu();
		subMenus.add(new ServerContextSubMenu(subMenu.toArray(new String[subMenu.size()]),
			staticItem.getId(), requestScript.getDefaults()));
	    } else {
		subMenus.add(new ServerContextSubMenu(new String[]{"Learn", "Pull", "Take"},
			staticItem.getId(), 3));
	    }

	}
	return new ServerContextMenu(subMenus.toArray(new ServerContextSubMenu[subMenus.size()]));
    }

    public static MessageActionsOffer getMessage(ServerContext context, StaticItem[] items) {
	return new MessageActionsOffer(getMenu(context, items));
    }

}
