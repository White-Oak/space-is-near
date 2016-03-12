package spaceisnear.game.messages;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 *
 * @author White Oak
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE) public class MessageActionsRequest extends Message implements NetworkableMessage {

    private int ids[];

    public MessageActionsRequest(int[] ids) {
	super(MessageType.CONTEXT_ACTIONS_REQUEST);
	this.ids = ids;
    }
}
