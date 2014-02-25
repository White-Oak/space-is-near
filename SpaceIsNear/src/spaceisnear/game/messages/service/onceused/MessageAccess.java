package spaceisnear.game.messages.service.onceused;

import lombok.Getter;
import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessageType;
import spaceisnear.game.messages.NetworkableMessage;

public class MessageAccess extends Message implements NetworkableMessage {

    @Getter private final boolean access;

    public MessageAccess(boolean access) {
	super(MessageType.ACCESS);
	this.access = access;
    }

}
