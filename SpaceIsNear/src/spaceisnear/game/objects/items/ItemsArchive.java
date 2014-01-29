/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects.items;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import spaceisnear.game.GameContext;
import spaceisnear.server.ServerContext;

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
    private final Image[] images;
    public static ItemsArchive itemsArchive;
    public static int PLITKA_ID;

    public ItemsArchive(ItemBundle[] bundles) throws SlickException, IOException {
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
	    if (itemBundle.name.equals("plitka")) {
		PLITKA_ID = i;
	    }
	}
	SpriteSheet sheet;
	try (InputStream resourceAsStream = getClass().getResourceAsStream("/res/sprites.png")) {
	    sheet = new SpriteSheet("sprites", resourceAsStream, GameContext.TILE_WIDTH,
		    GameContext.TILE_HEIGHT);
	}
	images = new Image[sheet.getHorizontalCount() * sheet.getVerticalCount()];
	for (int i = 0; i < sheet.getVerticalCount(); i++) {
	    for (int j = 0; j < sheet.getHorizontalCount(); j++) {
		images[i * sheet.getVerticalCount() + j] = sheet.getSprite(j, i);
	    }
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

    public Image getImage(int id) {
	return images[id];
    }

    public int[] getImageIds(int id) {
	return imageIds[id];
    }

    public spaceisnear.server.objects.items.StaticItem getNewItemForServer(int id, ServerContext serverContext) {
	return new spaceisnear.server.objects.items.StaticItem(serverContext, id);
    }

    public spaceisnear.game.objects.items.StaticItem getNewItem(int id, GameContext serverContext) {
	return new spaceisnear.game.objects.items.StaticItem(serverContext, id);
    }
}
