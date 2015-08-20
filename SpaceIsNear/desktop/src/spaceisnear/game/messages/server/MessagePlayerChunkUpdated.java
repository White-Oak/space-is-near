package spaceisnear.game.messages.server;

import spaceisnear.game.messages.DirectedMessage;
import spaceisnear.game.messages.MessageType;

/**
 *
 * @author White Oak
 */
public class MessagePlayerChunkUpdated extends DirectedMessage {

    public MessagePlayerChunkUpdated(int playerID) {
	super(MessageType.CHUNK_PLAYER_UPDATED, playerID);
    }

}
