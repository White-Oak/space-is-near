package spaceisnear.game.messages;

import lombok.Getter;

public class MessageInteracted extends DirectedMessage implements NetworkableMessage {

    @Getter private final int interactedWith;

    public MessageInteracted(int id, int interactedWith) {
	super(MessageType.INTERACTION, id);
	this.interactedWith = interactedWith;
    }

}
