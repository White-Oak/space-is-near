package spaceisnear.game.messages.server;

import lombok.Value;
import spaceisnear.game.messages.Message;
import spaceisnear.server.chunks.Chunk;
import spaceisnear.server.objects.ServerGameObject;

/**
 * Created by White Oak on 10.04.2015.
 */
@Value public class MessageObjectChangedChunk extends Message {
	int objectId;
	Chunk oldChunk, newChunk;
}
