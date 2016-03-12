package spaceisnear.game.messages.service.onceused;

import lombok.Getter;
import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessageType;

public class MessageError extends Message {

    @Getter private final String message;

    public MessageError(String message) {
	super(MessageType.ERROR);
	this.message = message;
    }

}
