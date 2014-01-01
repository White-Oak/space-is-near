/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.server.objects.items;

import spaceisnear.game.components.ItemPropertiesComponent;
import spaceisnear.game.components.PositionComponent;
import spaceisnear.game.objects.GameObjectType;
import spaceisnear.game.objects.Position;
import spaceisnear.server.ServerContext;
import spaceisnear.server.objects.ServerGameObject;

/**
 *
 * @author white_oak
 */
public class StaticItem extends ServerGameObject {

    private final ItemPropertiesComponent properties;

    public StaticItem(ServerContext context, Position p, int itemId) {
	super(GameObjectType.ITEM, context);
	PositionComponent pc = new PositionComponent(p);
	properties = new ItemPropertiesComponent(itemId);
	addComponents(pc, properties);
    }

    public StaticItem(ServerContext context, int itemId) {
	this(context, new Position(0, 0), itemId);
    }

    public ItemPropertiesComponent getProperties() {
	return properties;
    }

}