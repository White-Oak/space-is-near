/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components.client;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import spaceisnear.game.GameContext;
import spaceisnear.game.components.ComponentType;
import spaceisnear.game.components.ItemPropertiesComponent;
import spaceisnear.game.messages.*;
import spaceisnear.game.messages.properties.MessagePropertySet;
import spaceisnear.game.objects.items.ItemsArchive;
import spaceisnear.game.objects.items.StaticItem;

public class ItemPaintableComponent extends PaintableComponent {

    private int rotate;
    private MessageAnimationChanged animation;
    private int currentStep, currentPosition;
    private int animationStepsToNextStep;
    private int currentImageId;

    @Override
    public void paintComponent(SpriteBatch batch, int x, int y) {
	ItemPropertiesComponent properties = ((StaticItem) getOwner()).getProperties();
	int id = properties.getId();
	int[] imageIds = ItemsArchive.itemsArchive.getImageIds(id);
	//curently drawing zero state image
	TextureRegion image = ItemsArchive.itemsArchive.getTextureRegion(imageIds[currentImageId]);
	if (rotate != 0) {
	    batch.draw(image, x, y,
		    GameContext.TILE_WIDTH >> 1, GameContext.TILE_HEIGHT >> 1,
		    GameContext.TILE_WIDTH, GameContext.TILE_HEIGHT,
		    1, 1,
		    rotate, true);
	} else {
	    batch.draw(image, x, y);
	}
    }

    @Override
    public void processMessage(Message message) {
	switch (message.getMessageType()) {
	    case PROPERTY_SET:
		MessagePropertySet mps = (MessagePropertySet) message;
		if (mps.getName().equals("rotate")) {
		    rotate = Integer.parseInt((String) mps.getValue());
		}
		break;
	    case ANIMATION_CHANGED:
		animation = (MessageAnimationChanged) message;
		animationStepsToNextStep = animation.getAnimationTime() / MessageAnimationStep.STEP;
		currentStep = 0;
		currentPosition = 0;
		currentImageId = animation.getImageIds()[0];
		registerForAnimation();
		break;
	    case ANIMATION_STEP:
		if (animation != null) {
		    currentStep++;
		    if (currentStep % animationStepsToNextStep == 0) {
			currentPosition++;
			if (currentPosition < animation.getImageIds().length) {
			    currentImageId = animation.getImageIds()[currentPosition];
			} else {
			    animation = null;
			    unregisterForAnimation();
			}
		    }
		}
		break;
	}
    }

    public ItemPaintableComponent() {
	super(ComponentType.ITEM_PAINTABLE);
    }

}
