package spaceisnear.game.messages.properties;

import spaceisnear.game.messages.NetworkableMessage;

/**
 * Every message that changes a state of the game without creating new instances should implement this interface.
 *
 * @author White Oak
 */
public interface MessagePropertable extends NetworkableMessage {

    public int getId();
}
