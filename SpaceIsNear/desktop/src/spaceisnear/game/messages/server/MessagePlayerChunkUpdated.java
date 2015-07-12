package spaceisnear.game.messages.server;

import lombok.Getter;
import spaceisnear.abstracts.Context;
import spaceisnear.game.messages.DirectedMessage;
import spaceisnear.game.messages.MessageType;

/**
 *
 * @author White Oak
 */
public class MessagePlayerChunkUpdated extends DirectedMessage {

    @Getter private final int playerID;

    public MessagePlayerChunkUpdated(int playerID) {
	super(MessageType.CHUNK_PLAYER_UPDATED, Context.NETWORKING_ID);
	this.playerID = playerID;
    }

}
