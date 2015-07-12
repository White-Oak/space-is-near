package spaceisnear.game.messages;

/**
 * Message that should be broadcasted to everyone listening to this types of messages.
 *
 * @author White Oak
 */
public abstract class BroadcastMessage extends Message {

    public BroadcastMessage(MessageType arg0) {
	super(arg0);
    }

    @Override
    public boolean isDirected() {
	return false;
    }

}
