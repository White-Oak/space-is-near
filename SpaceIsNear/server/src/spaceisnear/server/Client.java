package spaceisnear.server;

import com.badlogic.gdx.utils.IntSet;
import com.esotericsoftware.kryonet.Connection;
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
    private IntSet hasObjects = new IntSet();

    public Client(Connection connection) {
	this.connection = connection;
    }

    public void createObjectAt(int id) {
	hasObjects.add(id);
    }

    public boolean hasObjectAt(int id) {
	return hasObjects.contains(id);
    }

    public boolean removeObjectAt(int id) {
	return hasObjects.remove(id);
    }

    public boolean playerChunkOutdated() {
	return newChunk != null;
    }

    public void clearCreatedCache() {
	hasObjects.clear();
    }

    public void dispose() {
	connection = null;
	clientInformation = null;
	playerInformation = null;
    }
}
