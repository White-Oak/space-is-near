package spaceisnear.server;

import com.esotericsoftware.kryonet.Connection;
import it.unimi.dsi.fastutil.ints.Int2IntAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import lombok.Data;
import spaceisnear.game.messages.service.onceused.MessageLogin;
import spaceisnear.game.messages.service.onceused.MessagePlayerInformation;
import spaceisnear.server.chunks.Chunk;
import spaceisnear.server.objects.Player;

/**
 *
 * @author White Oak
 */
@Data public class Client {

    private Connection connection;
    private MessageLogin clientInformation;
    private MessagePlayerInformation playerInformation;
    private Player player;
    private boolean rogered;
    private Chunk chunk;
    private Chunk newChunk;
    private Int2IntMap hasObjects = new Int2IntAVLTreeMap();

    {
	hasObjects.defaultReturnValue(-1);
    }

    public Client(Connection connection) {
	this.connection = connection;
    }

    public void createObjectAt(int id) {
	hasObjects.put(id, 1);
    }

    public boolean hasObjectAt(int id) {
	return hasObjects.containsKey(id);
    }

    public boolean removeObjectAt(int id) {
	return hasObjects.remove(id) != hasObjects.defaultReturnValue();
    }

    public boolean playerChunkOutdated() {
	return newChunk != null;
    }

    public void dispose() {
	connection = null;
	clientInformation = null;
	playerInformation = null;
    }
}
