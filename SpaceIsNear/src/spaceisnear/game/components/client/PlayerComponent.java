/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components.client;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import spaceisnear.game.components.ComponentType;
import spaceisnear.game.messages.Message;
import spaceisnear.game.objects.items.ItemsArchive;

/**
 *
 * @author LPzhelud
 */
public class PlayerComponent extends PaintableComponent {

    public PlayerComponent() {
	super(ComponentType.PLAYER);
    }

    @Override
    public void paintComponent(SpriteBatch batch, int x, int y) {
	int id = 2;
	int[] imageIds = ItemsArchive.itemsArchive.getImageIds(id);
	//curently drawing zero state image
	TextureRegion image = ItemsArchive.itemsArchive.getTextureRegion(imageIds[0]);
	batch.draw(image, x, y);
    }

    @Override
    public void processMessage(Message message) {
    }
}
