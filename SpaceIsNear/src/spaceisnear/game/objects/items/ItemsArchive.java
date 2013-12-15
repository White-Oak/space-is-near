/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects.items;

import java.util.HashMap;

/**
 *
 * @author White Oak
 */
public class ItemsArchive {

    private final HashMap<String, Integer> ids = new HashMap<>();
    private final boolean[] blockingAir;
    private final boolean[] blockingPath;
    private final Size[] sizes;
    private final String[] names;
    private final Type[] types;
    private final int[][] imageIds;
    public static ItemsArchive itemsArchive;

    public ItemsArchive(ItemBundle[] bundles) {
	final int length = bundles.length;
	blockingAir = new boolean[length];
	blockingPath = new boolean[length];
	sizes = new Size[length];
	names = new String[length];
	types = new Type[length];
	imageIds = new int[length][];
	for (int i = 0; i < length; i++) {
	    ItemBundle itemBundle = bundles[i];
	    blockingAir[i] = itemBundle.blockingAir;
	    blockingPath[i] = itemBundle.blockingPath;
	    sizes[i] = itemBundle.size;
	    names[i] = itemBundle.name;
	    types[i] = itemBundle.type;
	    imageIds[i] = itemBundle.imageIds;
	    ids.put(itemBundle.name, i);
	}
    }

    public int getIdByName(String name) {
	return ids.get(name);
    }

    public boolean isBlockingAir(int id) {
	return blockingAir[id];
    }

    public boolean isBlockingAir(String name) {
	return isBlockingAir(getIdByName(name));
    }

    public boolean isBlockingPath(int id) {
	return blockingPath[id];
    }

    public boolean isBlockingPath(String name) {
	return isBlockingPath(getIdByName(name));
    }

    public Size getSize(int id) {
	return sizes[id];
    }

    public Size getSize(String name) {
	return sizes[getIdByName(name)];
    }

    public String getName(int id) {
	return names[id];
    }

    public String getName(String name) {
	return name;
    }

    public Type getType(int id) {
	return types[id];
    }

    public Type getType(String name) {
	return types[getIdByName(name)];
    }
}
