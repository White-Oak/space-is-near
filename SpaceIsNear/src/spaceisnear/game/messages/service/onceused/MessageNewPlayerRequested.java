package spaceisnear.game.messages.service.onceused;

import spaceisnear.game.messages.*;

public class MessageNewPlayerRequested extends Message implements NetworkableMessage {

    public MessageNewPlayerRequested() {
	super(MessageType.NEW_PLAYER_REQUESTED);
    }

}
