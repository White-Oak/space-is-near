package spaceisnear.game.messages;

public class MessageInteraction extends DirectedMessage {

    public MessageInteraction(int id) {
	super(MessageType.INTERACTED, id);
    }

}
