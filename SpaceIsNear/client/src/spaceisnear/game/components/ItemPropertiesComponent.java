/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import lombok.Getter;
import spaceisnear.abstracts.ItemsArchivable;
import spaceisnear.game.messages.Message;
import spaceisnear.game.objects.items.*;

public class ItemPropertiesComponent extends Component {

    private static ItemsArchivable archivable;
    @Getter private final int id;

    static {
	archivable = getItemsArchive();
    }

    @Override
    public void processMessage(Message message) {
    }

    public ItemPropertiesComponent(int id) {
	super(ComponentType.ITEM_PROPERTIES);
	this.id = id;
    }

    ItemPropertiesComponent() {
	super(ComponentType.ITEM_PROPERTIES);
	id = -1;
    }

    public boolean isBlockingAir() {
	return getBundle().blockingAir;
    }

    public boolean isBlockingPath() {
	return getBundle().blockingPath;
    }

    public Size getSize() {
	return getBundle().size;
    }

    public String getName() {
	return getBundle().name;
    }

    public Type getTypeOfItem() {
	return getBundle().type;
    }

    public ItemBundle getBundle() {
	return archivable.getBundle(getId());
    }

    public String getDescription() {
	return getBundle().description;
    }

    public int getZLevel() {
	return getBundle().z;
    }

    private static ItemsArchivable getItemsArchive() {
	return ItemsArchive.itemsArchive;
    }
}
