package spaceisnear.game.messages.service.onceused;

import spaceisnear.game.messages.*;

public class MessageJoined extends Message implements NetworkableMessage {

    public MessageJoined() {
	super(MessageType.JOINED);
    }

}
