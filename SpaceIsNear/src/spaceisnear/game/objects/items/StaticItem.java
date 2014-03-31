/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects.items;

import java.util.List;
import spaceisnear.game.GameContext;
import spaceisnear.game.components.*;
import spaceisnear.game.components.client.ItemPaintableComponent;
import spaceisnear.game.components.client.LightComponent;
import spaceisnear.game.messages.properties.MessagePropertySet;
import spaceisnear.game.objects.*;

/**
 *
 * @author white_oak
 */
public class StaticItem extends ClientGameObject {

    private final ItemPropertiesComponent properties;

    public StaticItem(Position p, int itemId) {
	super(GameObjectType.ITEM);
	PositionComponent pc = new PositionComponent(p);
	properties = new ItemPropertiesComponent(itemId);
	addComponents(pc, properties, new ItemPaintableComponent(), new LightComponent());
	message(new MessagePropertySet(getId(), "blockingLight", ItemsArchive.itemsArchive.getBundle(itemId).blockingLight));
    }

    public StaticItem(int itemId) {
	this(new Position(0, 0), itemId);
    }

    private StaticItem(GameContext context, Component[] components) {
	super(GameObjectType.ITEM);
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
