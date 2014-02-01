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
import spaceisnear.ItemsArchivable;
import spaceisnear.game.GameContext;
import spaceisnear.server.ServerContext;

/**
 *
 * @author White Oak
 */
public class ItemsArchive implements ItemsArchivable {

    private final HashMap<String, Integer> ids = new HashMap<>();
    private final Image[] images;
    private final ItemBundle[] bundles;
    public static ItemsArchive itemsArchive;
    public static int PLITKA_ID;

    public ItemsArchive(ItemBundle[] bundles) throws SlickException, IOException {
	final int length = bundles.length;
	this.bundles = bundles;
	for (int i = 0; i < length; i++) {
	    ItemBundle itemBundle = bundles[i];
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

    public Image getImage(int id) {
	return images[id];
    }

    @Override
    public int[] getImageIds(int id) {
	return bundles[id].imageIds;
    }

    @Override
    public ItemBundle getBundle(int id) {
	return bundles[id];
    }

    public spaceisnear.game.objects.items.StaticItem getNewItem(int id, GameContext serverContext) {
	return new spaceisnear.game.objects.items.StaticItem(serverContext, id);
    }
}
