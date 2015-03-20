package spaceisnear.game.components.server.context;

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
	for (StaticItem staticItem : items) {
	    final ScriptsManager scriptsManager = context.getScriptsManager();
	    final ContextRequestScript requestScript = (ContextRequestScript) scriptsManager.getScriptFor(
		    ScriptsManager.ScriptType.CONREQ, staticItem.getProperties().getName());
	    if (requestScript != null) {
		requestScript.init(context, staticItem);
		requestScript.script();
		ArrayList<String> subMenu = requestScript.getSubMenu();
		subMenus.add(new ServerContextSubMenu(subMenu.toArray(new String[subMenu.size()]),
			staticItem.getId()));
	    } else {
		subMenus.add(new ServerContextSubMenu(ACTIONS_DEFAULT, staticItem.getId()));
	    }
	}
	return new ServerContextMenu(subMenus.toArray(new ServerContextSubMenu[subMenus.size()]));
    }
    private static final String[] ACTIONS_DEFAULT = new String[]{"Learn", "Pull", "Take"};

    public static MessageActionsOffer getMessage(ServerContext context, StaticItem[] items) {
	return new MessageActionsOffer(getMenu(context, items));
    }

}
