package spaceisnear.game.messages;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 *
 * @author White Oak
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE) public class MessageActionChosen extends Message implements NetworkableMessage {

    private int chosen, chosenItemId;

    public MessageActionChosen(int chosen, int chosenItem) {
	super(MessageType.CONTEXT_ACTION_CHOSEN);
	this.chosen = chosen;
	this.chosenItemId = chosenItem;
    }
}
