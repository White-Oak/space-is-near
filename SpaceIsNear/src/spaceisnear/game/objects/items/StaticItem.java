/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects.items;

import java.util.List;
import spaceisnear.game.GameContext;
import spaceisnear.game.bundles.ObjectBundle;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentType;
import spaceisnear.game.components.client.ItemPaintableComponent;
import spaceisnear.game.components.ItemPropertiesComponent;
import spaceisnear.game.components.PositionComponent;
import spaceisnear.game.objects.ClientGameObject;
import spaceisnear.game.objects.GameObjectState;
import spaceisnear.game.objects.GameObjectType;
import spaceisnear.game.objects.Position;

/**
 *
 * @author white_oak
 */
public class StaticItem extends ClientGameObject {

    private final ItemPropertiesComponent properties;

    public StaticItem(GameContext context, Position p, int itemId) {
	super(GameObjectType.ITEM, context);
	PositionComponent pc = new PositionComponent(p);
	properties = new ItemPropertiesComponent(itemId);
	addComponents(pc, properties, new ItemPaintableComponent());
    }

    public StaticItem(GameContext context, int itemId) {
	this(context, new Position(0, 0), itemId);
    }

    private StaticItem(GameContext context, Component[] components) {
	super(GameObjectType.ITEM, context);
	addComponents(components);
	addComponents(new ItemPaintableComponent());
	List<Component> components1 = getComponents();
	ItemPropertiesComponent ipc = null;
	for (Component component : components1) {
	    if (component.getType() == ComponentType.ITEM_PROPERTIES) {
		ipc = (ItemPropertiesComponent) component;
		break;
	    }
	}
	properties = ipc;
    }

    public ItemPropertiesComponent getProperties() {
	return properties;
    }

}
