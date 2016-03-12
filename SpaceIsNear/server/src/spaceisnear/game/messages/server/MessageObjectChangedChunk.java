package spaceisnear.game.messages.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import spaceisnear.game.messages.Message;
import spaceisnear.server.chunks.Chunk;

/**
 * Created by White Oak on 10.04.2015.
 */
@Getter @AllArgsConstructor public class MessageObjectChangedChunk extends Message {

    final int objectId;
    final Chunk oldChunk, newChunk;
}
