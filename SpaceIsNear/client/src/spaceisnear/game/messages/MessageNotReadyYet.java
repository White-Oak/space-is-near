package spaceisnear.game.messages;

import lombok.Getter;
import spaceisnear.game.GameContext;
import spaceisnear.game.messages.properties.MessagePropertable;

/**
 *
 * @author White Oak
 */
public class MessageNotReadyYet extends DirectedMessage {

    @Getter private final MessagePropertable message;
    @Getter private int times;

    public MessageNotReadyYet(MessagePropertable message, int times) {
	super(MessageType.NOT_READY_YET, GameContext.PATIENCE_ID);
	this.message = message;
	this.times = times;
    }

    public MessageNotReadyYet(MessagePropertable message) {
	this(message, 0);
    }

    public MessageNotReadyYet onceAgain() {
	times++;
	return this;
    }

}
