package spaceisnear.game.messages;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE) public class MessageInteracted extends DirectedMessage implements NetworkableMessage {

    @Getter private int interactedWith;

    public MessageInteracted(int id, int interactedWith) {
	super(MessageType.INTERACTION, id);
	this.interactedWith = interactedWith;
    }

}
