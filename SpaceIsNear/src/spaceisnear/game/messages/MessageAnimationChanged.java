package spaceisnear.game.messages;

import lombok.Getter;

public class MessageAnimationChanged extends DirectedMessage implements NetworkableMessage {

    @Getter private final int[] imageIds;
    @Getter private final int animationTime;

    public MessageAnimationChanged(int id, int[] imageIds, int animationTime) {
	super(MessageType.ANIMATION_CHANGED, id);
	this.imageIds = imageIds;
	this.animationTime = animationTime;
    }
}
