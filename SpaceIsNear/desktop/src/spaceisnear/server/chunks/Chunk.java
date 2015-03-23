package spaceisnear.server.chunks;

import lombok.Value;

/**
 *
 * @author White Oak
 */
@Value public class Chunk {

    public static final int CHUNK_SIZE = 11;
    private int x, y;
}
