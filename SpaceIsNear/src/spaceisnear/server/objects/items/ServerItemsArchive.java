/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.server.objects.items;

import spaceisnear.abstracts.ItemsArchivable;
import spaceisnear.game.objects.items.*;
import spaceisnear.server.ServerContext;

/**
 *
 * @author White Oak
 */
public class ServerItemsArchive extends ItemsArchivable {

    public static ServerItemsArchive ITEMS_ARCHIVE;

    public ServerItemsArchive(ItemBundle[] bundles) {
	super(bundles);
    }

    public spaceisnear.server.objects.items.StaticItem getNewItemForServer(int id, ServerContext serverContext) {
	return new spaceisnear.server.objects.items.StaticItem(serverContext, id);
    }
}
