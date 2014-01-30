/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components.client;

import spaceisnear.game.components.client.PaintableComponent;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import spaceisnear.game.components.ComponentType;
import spaceisnear.game.components.ItemPropertiesComponent;
import spaceisnear.game.messages.Message;
import spaceisnear.game.objects.items.ItemsArchive;
import spaceisnear.game.objects.items.StaticItem;

public class ItemPaintableComponent extends PaintableComponent {

    @Override
    public void paintComponent(Graphics g) {
	ItemPropertiesComponent properties = ((StaticItem) getOwner()).getProperties();
	int id = properties.getId();
	int[] imageIds = ItemsArchive.itemsArchive.getImageIds(id);
	//curently drawing zero state image
	Image image = ItemsArchive.itemsArchive.getImage(imageIds[0]);
	g.drawImage(image, 0, 0);
    }

    @Override
    public void processMessage(Message message) {
    }

    public ItemPaintableComponent() {
	super(ComponentType.ITEM_PAINTABLE);
    }

}
