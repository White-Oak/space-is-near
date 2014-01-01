/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import spaceisnear.game.messages.Message;
import spaceisnear.game.objects.items.ItemsArchive;
import spaceisnear.game.objects.items.Type;

public class ItemPropertiesComponent extends Component {

    @Override
    public void processMessage(Message message) {
    }

    public ItemPropertiesComponent(int id) {
	super(ComponentType.ITEM_PROPERTY);
	addState(new ComponentState("id", id));
    }

    ItemPropertiesComponent() {
	super(ComponentType.ITEM_PROPERTY);
    }

    public int getId() {
	return (int) getStates().get("id").getValue();
    }

    public Type getTypeOfItem() {
	return ItemsArchive.itemsArchive.getType(getId());
    }
}
