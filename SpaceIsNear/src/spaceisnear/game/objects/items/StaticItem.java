/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects.items;

import java.util.List;
import spaceisnear.Utils;
import spaceisnear.game.GameContext;
import spaceisnear.game.components.*;
import spaceisnear.game.components.client.*;
import spaceisnear.game.messages.properties.MessagePropertySet;
import spaceisnear.game.objects.*;
import spaceisnear.game.objects.items.ItemBundle.Property;

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
	addComponents(pc, properties, new ItemPaintableComponent(), new ShadowComponent());
	message(new MessagePropertySet(getId(), "blockingLight", ItemsArchive.itemsArchive.getBundle(itemId).blockingLight));
    }

    @Override
    public void setContext(GameContext context) {
	super.setContext(context); //To change body of generated methods, choose Tools | Templates.
	Property[] propertys = ItemsArchive.itemsArchive.getBundle(getProperties().getId()).defaultProperties;
	if (propertys != null) {
	    for (Property property : propertys) {
		if (property.getName().equals("light")) {
		    LightComponent.LightProperty lightProp;
		    lightProp = Utils.GSON.fromJson((String) property.getValue(), LightComponent.LightProperty.class);
		    addComponents(new LightComponent(lightProp, GameContext.getRayHandler()));
		}
	    }
	}
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
