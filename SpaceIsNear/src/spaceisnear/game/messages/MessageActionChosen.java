package spaceisnear.game.messages;

/**
 *
 * @author White Oak
 */
public class MessageActionChosen extends Message {

    private final int chosen;

    public MessageActionChosen(int chosen) {
	super(MessageType.CONTEXT_ACTION_CHOSEN);
	this.chosen = chosen;
    }

}
