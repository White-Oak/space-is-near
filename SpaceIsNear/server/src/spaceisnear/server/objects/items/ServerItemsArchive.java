package spaceisnear.server.objects.items;

import spaceisnear.abstracts.ItemsArchivable;
import spaceisnear.game.objects.items.*;
import spaceisnear.server.ServerContext;
import spaceisnear.server.scriptsv2.ItemScriptBundle;

/**
 *
 * @author White Oak
 */
public class ServerItemsArchive extends ItemsArchivable {

    public static ServerItemsArchive ITEMS_ARCHIVE;

    public ServerItemsArchive(ItemBundle[] bundles, ItemScriptBundle[] scripts) {
	super(bundles);
    }

    public spaceisnear.server.objects.items.StaticItem getNewItemForServer(int id, ServerContext serverContext) {
	return new spaceisnear.server.objects.items.StaticItem(serverContext, id);
    }
}
