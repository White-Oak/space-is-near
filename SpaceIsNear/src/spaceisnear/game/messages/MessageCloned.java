package spaceisnear.game.messages;

public class MessageCloned extends Message implements NetworkableMessage {

    public int amount;

    public MessageCloned() {
	super(MessageType.CLONED);
    }
}
