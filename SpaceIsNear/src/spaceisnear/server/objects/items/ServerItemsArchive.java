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

import spaceisnear.game.objects.items.*;
import java.io.IOException;
import java.util.HashMap;
import org.newdawn.slick.SlickException;
import spaceisnear.ItemsArchivable;
import spaceisnear.server.ServerContext;

/**
 *
 * @author White Oak
 */
public class ServerItemsArchive implements ItemsArchivable {

    private final HashMap<String, Integer> ids = new HashMap<>();
    private final ItemBundle[] bundles;
    public static ServerItemsArchive itemsArchive;
    public static int PLITKA_ID;
    public static int RADIO_ID;

    public ServerItemsArchive(ItemBundle[] bundles) throws SlickException, IOException {
	final int length = bundles.length;
	this.bundles = bundles;
	for (int i = 0; i < length; i++) {
	    ItemBundle itemBundle = bundles[i];
	    ids.put(itemBundle.name, i);
	    switch (itemBundle.name) {
		case "plitka":
		    PLITKA_ID = i;
		    break;
		case "radio":
		    RADIO_ID = i;
		    break;
	    }
	}
    }

    @Override
    public int getIdByName(String name) {
	return ids.get(name);
    }

    @Override
    public boolean isBlockingAir(int id) {
	return bundles[id].blockingAir;
    }

    @Override
    public boolean isBlockingAir(String name) {
	return isBlockingAir(getIdByName(name));
    }

    @Override
    public boolean isBlockingPath(int id) {
	return bundles[id].blockingPath;
    }

    @Override
    public boolean isBlockingPath(String name) {
	return isBlockingPath(getIdByName(name));
    }

    @Override
    public Size getSize(int id) {
	return bundles[id].size;
    }

    @Override
    public Size getSize(String name) {
	return getSize(getIdByName(name));
    }

    @Override
    public String getName(int id) {
	return bundles[id].name;
    }

    @Override
    public String getName(String name) {
	return name;
    }

    @Override
    public Type getType(int id) {
	return bundles[id].type;
    }

    @Override
    public Type getType(String name) {
	return getType(getIdByName(name));
    }

    @Override
    public int[] getImageIds(int id) {
	return bundles[id].imageIds;
    }

    @Override
    public ItemBundle getBundle(int id) {
	return bundles[id];
    }

    public spaceisnear.server.objects.items.StaticItem getNewItemForServer(int id, ServerContext serverContext) {
	return new spaceisnear.server.objects.items.StaticItem(serverContext, id);
    }
}
