/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import spaceisnear.ItemsArchivable;
import spaceisnear.game.messages.Message;
import spaceisnear.game.objects.items.ItemBundle;
import spaceisnear.game.objects.items.ItemsArchive;
import spaceisnear.game.objects.items.Size;
import spaceisnear.game.objects.items.Type;
import spaceisnear.server.objects.items.ServerItemsArchive;

public class ItemPropertiesComponent extends Component {

    private static ItemsArchivable archivable;

    static {
	archivable = getItemsArchive();
    }

    @Override
    public void processMessage(Message message) {
    }

    public ItemPropertiesComponent(int id) {
	super(ComponentType.ITEM_PROPERTIES);
	addState(new ComponentState("id", id));
    }

    ItemPropertiesComponent() {
	super(ComponentType.ITEM_PROPERTIES);
    }

    public int getId() {
	return (int) getStateNamed("id").getValue();
    }

    public boolean isBlockingAir() {
	return archivable.isBlockingAir(getId());
    }

    public boolean isBlockingPath() {
	return archivable.isBlockingPath(getId());
    }

    public Size getSize() {
	return archivable.getSize(getId());
    }

    public String getName() {
	return archivable.getName(getId());
    }

    public Type getTypeOfItem() {
	return archivable.getType(getId());
    }

    public ItemBundle getBundle() {
	return archivable.getBundle(getId());
    }

    private static ItemsArchivable getItemsArchive() {
	if (ItemsArchive.itemsArchive != null) {
	    return ItemsArchive.itemsArchive;
	} else {
	    return ServerItemsArchive.itemsArchive;
	}
    }
}
