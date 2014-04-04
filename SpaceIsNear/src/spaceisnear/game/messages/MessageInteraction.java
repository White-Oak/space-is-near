package spaceisnear.game.messages;

import lombok.Getter;

public class MessageInteraction extends DirectedMessage {

    @Getter private final int interactedWith;

    public MessageInteraction(int id, int interactedWith) {
	super(MessageType.INTERACTED, id);
	this.interactedWith = interactedWith;
    }

}
