package spaceisnear.server;

import java.util.List;
import lombok.AllArgsConstructor;
import spaceisnear.game.messages.MessageCreated;
import spaceisnear.game.messages.properties.MessagePropertable;

/**
 *
 * @author White Oak
 */
@AllArgsConstructor public class ObjectMessaged {

    public MessageCreated created;
    public List<MessagePropertable> propertables;
}
