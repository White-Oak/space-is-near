package spaceisnear.server;

import com.esotericsoftware.kryonet.Connection;
import lombok.Data;
import spaceisnear.game.messages.service.onceused.MessageLogin;
import spaceisnear.game.messages.service.onceused.MessagePlayerInformation;
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

    public Client(Connection connection) {
	this.connection = connection;
    }

    public void dispose() {
	connection = null;
	clientInformation = null;
	playerInformation = null;
    }
}
