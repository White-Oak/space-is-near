package spaceisnear.game.messages.server;

import lombok.Getter;
import spaceisnear.abstracts.Context;
import spaceisnear.game.messages.DirectedMessage;
import spaceisnear.game.messages.MessageType;

/**
 *
 * @author White Oak
 */
public class MessageChunkUpdated extends DirectedMessage {

    @Getter private final int playerID;

    public MessageChunkUpdated(int playerID) {
	super(MessageType.CHUNK_UPDATED, Context.NETWORKING_ID);
	this.playerID = playerID;
    }

}
