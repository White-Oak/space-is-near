package spaceisnear.server.chunks;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.collections4.CollectionUtils;
import spaceisnear.abstracts.AbstractGameObject;
import spaceisnear.game.components.PositionComponent;
import spaceisnear.game.ui.Position;

/**
 *
 * @author White Oak
 */
public class ChunkManager {

    private final Chunk[][] chunks;
    private final int width, height;

    public ChunkManager(int worldWidth, int worldHeight) {
	width = worldWidth / Chunk.CHUNK_SIZE;
	height = worldHeight / Chunk.CHUNK_SIZE;
	chunks = new Chunk[width][height];
	for (int i = 0; i < chunks.length; i++) {
	    Chunk[] chunk = chunks[i];
	    for (int j = 0; j < chunk.length; j++) {
		chunk[j] = new Chunk(i * Chunk.CHUNK_SIZE, j * Chunk.CHUNK_SIZE);
	    }
	}
    }

    private Chunk getChunk(int chunkX, int chunkY) {
	return chunks[chunkX][chunkY];
    }

    /**
     * Returns a chunk fitting the given position.
     *
     * @param x
     * @param y
     * @return
     */
    public Chunk getChunkByPosition(int x, int y) {
	return getChunk(x / Chunk.CHUNK_SIZE, y / Chunk.CHUNK_SIZE);
    }

    /**
     * Returns a chunk fitting the given position.
     *
     * @param position
     * @return
     */
    public Chunk getChunkByPosition(Position position) {
	return getChunkByPosition(position.getX(), position.getY());
    }

    public boolean isInChunk(Chunk chunk, int x, int y) {
	return getChunkByPosition(x, y).equals(chunk);
    }

    public boolean isInChunk(Chunk chunk, Position p) {
	return getChunkByPosition(p).equals(chunk);
    }

    public boolean isNearChunk(Chunk chunk, Position p) {
	return isNearChunk(chunk, p.getX(), p.getY());
    }

    public boolean isNearChunk(Chunk chunk, int x, int y) {
	final int x1 = chunk.getX();
	final int y1 = chunk.getY();
	return ((x > x1 - Chunk.CHUNK_SIZE && x < x1 + Chunk.CHUNK_SIZE * 2)
		&& (y > y1 - Chunk.CHUNK_SIZE && y < y1 + Chunk.CHUNK_SIZE * 2));
    }

    public boolean isNearChunk(Chunk chunk, AbstractGameObject object) {
	PositionComponent positionComponent = object.getPositionComponent();
	if (positionComponent != null) {
	    return isNearChunk(chunk, positionComponent.getPosition());
	}
	return false;
    }

    public boolean isNearChunk(Chunk chunk1, Chunk chunk2) {
	return isNearChunk(chunk1, chunk2.getX(), chunk2.getY());
    }

    public Collection<Chunk> getChunksNear(Chunk chunk) {
	ArrayList<Chunk> chunkerino = new ArrayList<>(9);
	if (chunk != null) {
	    for (int i = chunk.getX() - Chunk.CHUNK_SIZE, ic = 0; ic <= 2; i += Chunk.CHUNK_SIZE, ic++) {
		for (int j = chunk.getY() - Chunk.CHUNK_SIZE, jc = 0; jc <= 2; j += Chunk.CHUNK_SIZE, jc++) {
		    try {
			chunkerino.add(getChunkByPosition(i, j));
		    } catch (ArrayIndexOutOfBoundsException e) {
		    }
		}
	    }
	}
	assert chunkerino.size() > 3;
	return chunkerino;
    }

    public Collection<Chunk> substractEnvironments(Chunk from, Chunk what) {
	return CollectionUtils.subtract(getChunksNear(from), getChunksNear(what));
    }
}
