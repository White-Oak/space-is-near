/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.editor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.List;
import lombok.Data;
import spaceisnear.game.GameContext;

/**
 *
 * @author White Oak
 */
@Data public class MapAction {

    private final Type type;
    private final Item item;
    private int fillX, fillY;

    public static enum Type {

	FILL, ADD, DELETE
    }

    private void addItem(List<Item> items, Item item) {
	if (item.getX() >= 0 && item.getX() < 64) {
	    if (item.getY() >= 0 && item.getY() < 64) {
		items.add(item);
	    }
	}
    }

    public void act(List<Item> items) {
	switch (type) {
	    case FILL:
		int deltaX = item.getX() < fillX ? 1 : -1;
		int deltaY = item.getY() < fillY ? 1 : -1;
		for (int i = item.getX(); i != fillX; i += deltaX) {
		    for (int j = item.getY(); j != fillY; j += deltaY) {
			addItem(items, new Item(item.getId(), i, j));
		    }
		}
		break;
	    case ADD:
		addItem(items, item);
		break;
	    case DELETE:
		for (int i = items.size() - 1; i > 0; i--) {
		    Item item1 = items.get(i);
		    if (item1.getX() == item.getX() && item1.getY() == item.getY()) {
			items.remove(i);
			item.setId(item1.getId());
		    }
		}

	}
    }

    public void draw(SpriteBatch batch, int x, int y) {
	final ItemsHandler handler = ItemsHandler.HANDLER;
	switch (type) {
	    case ADD:
		batch.draw(handler.getTextureRegion(item.getId()),
			item.getX() * GameContext.TILE_WIDTH + x, item.getY() * GameContext.TILE_HEIGHT + y);
		break;
	    case FILL:
		int deltaX = item.getX() < fillX ? 1 : -1;
		int deltaY = item.getY() < fillY ? 1 : -1;
		for (int i = item.getX(); i != fillX; i += deltaX) {
		    for (int j = item.getY(); j != fillY; j += deltaY) {
			batch.draw(handler.getTextureRegion(item.getId()),
				(i) * GameContext.TILE_WIDTH + x, (j) * GameContext.TILE_HEIGHT + y);
		    }
		}
		break;

	}
    }
}
