package spaceisnear.game.components.server;

import me.whiteoak.minlog.Log;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentType;
import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.server.MessageObjectChangedChunk;
import spaceisnear.server.chunks.Chunk;
import spaceisnear.server.chunks.ChunkManager;

/**
 * Created by White Oak on 09.04.2015.
 */
public class PlayerChunkObserverComponent extends Component {

    public PlayerChunkObserverComponent() {
	super(ComponentType.PLAYER_CHUNK_OBSERVER);
    }

    @Override
    public void processMessage(Message message) {
	switch (message.getMessageType()) {
	    case CHUNK_OBJECT_UPDATED: {
		final MessageObjectChangedChunk mocc = (MessageObjectChangedChunk) message;
		if (checkIfObjectIsNearNow(mocc)) {
		    //TODO send MessageCreated to player
		    //and his properties???
		    Log.error("server", "He was not ready yet.");
		    assert false : "IM NOT READY YET";
		}
	    }
	    break;
	    case CHUNK_PLAYER_UPDATED: {
	    }
	    break;
	}
    }

    private boolean checkIfObjectIsNearNow(MessageObjectChangedChunk mocc) {
	final Chunk chunk = (Chunk) getStateValueNamed("chunk");
	final ChunkManager chunkManager = getChunkManager();
	if (!chunkManager.isNearChunk(chunk, mocc.getOldChunk())) {
	    if (chunkManager.isNearChunk(chunk, mocc.getNewChunk())) {
		return true;
	    }
	}
	return false;
    }
}
