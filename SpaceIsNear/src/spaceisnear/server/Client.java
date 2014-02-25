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

    private final Connection connection;
    private MessageClientInformation clientInformation;
    private MessagePlayerInformation playerInformation;
    private Player player;
}
