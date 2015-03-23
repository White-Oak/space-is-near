/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.abstracts;

import java.util.HashMap;
import spaceisnear.game.objects.items.*;

/**
 *
 * @author White Oak
 */
public abstract class ItemsArchivable {

    private final HashMap<String, Integer> ids = new HashMap<>();
    private final ItemBundle[] bundles;
    public static int PLITKA_ID;
    public static int RADIO_ID;

    public ItemsArchivable(ItemBundle[] bundles) {
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

    public int getIdByName(String name) {
	return ids.get(name);
    }

    public boolean isBlockingAir(int id) {
	return bundles[id].blockingAir;
    }

    public boolean isBlockingAir(String name) {
	return isBlockingAir(getIdByName(name));
    }

    public boolean isBlockingPath(int id) {
	return bundles[id].blockingPath;
    }

    public boolean isBlockingPath(String name) {
	return isBlockingPath(getIdByName(name));
    }

    public Size getSize(int id) {
	return bundles[id].size;
    }

    public Size getSize(String name) {
	return getSize(getIdByName(name));
    }

    public String getName(int id) {
	return bundles[id].name;
    }

    public String getName(String name) {
	return name;
    }

    public Type getType(int id) {
	return bundles[id].type;
    }

    public Type getType(String name) {
	return getType(getIdByName(name));
    }

    public int[] getImageIds(int id) {
	return bundles[id].imageIds;
    }

    public ItemBundle getBundle(int id) {
	return bundles[id];
    }
}
