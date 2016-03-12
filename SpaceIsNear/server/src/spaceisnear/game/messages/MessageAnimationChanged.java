package spaceisnear.game.messages;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE) public class MessageAnimationChanged extends DirectedMessage implements NetworkableMessage {

    @Getter private int[] imageIds;
    @Getter private int animationTime;

    public MessageAnimationChanged(int id, int[] imageIds, int animationTime) {
	super(MessageType.ANIMATION_CHANGED, id);
	this.imageIds = imageIds;
	this.animationTime = animationTime;
    }
}
