/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components.client;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import spaceisnear.game.components.ComponentType;
import spaceisnear.game.components.ItemPropertiesComponent;
import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.properties.MessagePropertySet;
import spaceisnear.game.objects.items.ItemsArchive;
import spaceisnear.game.objects.items.StaticItem;

public class ItemPaintableComponent extends PaintableComponent {

    private int rotate;

    @Override
    public void paintComponent(Graphics g) {
	ItemPropertiesComponent properties = ((StaticItem) getOwner()).getProperties();
	int id = properties.getId();
	int[] imageIds = ItemsArchive.itemsArchive.getImageIds(id);
	//curently drawing zero state image
	Image image = ItemsArchive.itemsArchive.getImage(imageIds[0]);
	image.rotate(rotate);
	g.drawImage(image, 0, 0);
	image.rotate(-rotate);
    }

    @Override
    public void processMessage(Message message) {
	switch (message.getMessageType()) {
	    case PROPERTY_SET:
		MessagePropertySet mps = (MessagePropertySet) message;
		if (mps.getName().equals("rotate")) {
		    rotate = (int) mps.getValue();
		}
		break;
	}
    }

    public ItemPaintableComponent() {
	super(ComponentType.ITEM_PAINTABLE);
    }

}
