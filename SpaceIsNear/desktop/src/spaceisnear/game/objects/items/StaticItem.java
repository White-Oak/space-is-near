/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects.items;

import java.util.List;
import spaceisnear.Utils;
import spaceisnear.game.Corev2;
import spaceisnear.game.GameContext;
import spaceisnear.game.components.*;
import spaceisnear.game.components.client.*;
import spaceisnear.game.messages.properties.MessagePropertySet;
import spaceisnear.game.objects.ClientGameObject;
import spaceisnear.game.objects.GameObjectType;
import spaceisnear.game.objects.items.ItemBundle.Property;
import spaceisnear.game.ui.Position;

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
	final ItemPaintableComponent itemPaintableComponent = new ItemPaintableComponent();
	addComponents(pc, properties, itemPaintableComponent, new ShadowComponent());
	itemPaintableComponent.setZLayer(properties.getZLevel());
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

    @Override
    public void setContext(GameContext context) {
	super.setContext(context); //To change body of generated methods, choose Tools | Templates.
	Property[] propertys = ItemsArchive.itemsArchive.getBundle(getProperties().getId()).defaultProperties;
	if (propertys != null) {
	    for (Property property : propertys) {
		if (property.getName().equals("light")) {
		    LightComponent.LightProperty lightProp;
		    lightProp = Utils.GSON.fromJson((String) property.getValue(), LightComponent.LightProperty.class);
		    addComponents(new LightComponent(lightProp, Corev2.rayHandler));
		}
	    }
	}
    }

    public ItemPropertiesComponent getProperties() {
	return properties;
    }

}
