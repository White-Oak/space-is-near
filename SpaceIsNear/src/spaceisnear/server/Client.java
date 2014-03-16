package spaceisnear.server;

import com.esotericsoftware.kryonet.Connection;
import lombok.Data;
import spaceisnear.game.messages.service.onceused.MessageClientInformation;
import spaceisnear.game.messages.service.onceused.MessagePlayerInformation;
import spaceisnear.server.objects.Player;

/**
 *
 * @author White Oak
 */
@Data public class Client {

    private Connection connection;
    private MessageClientInformation clientInformation;
    private MessagePlayerInformation playerInformation;
    private Player player;
    private boolean rogered;

    public Client(Connection connection) {
	this.connection = connection;
    }

    public void dispose() {
	connection = null;
	clientInformation = null;
	playerInformation = null;
    }
}
