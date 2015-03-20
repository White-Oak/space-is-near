package spaceisnear.game.messages;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import spaceisnear.server.Client;
import spaceisnear.server.ServerContext;

/**
 *
 * @author White Oak
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE) public class MessageActionChosen extends Message implements NetworkableMessage {

    private int chosen, chosenItem;

    public MessageActionChosen(int chosen, int chosenItem) {
	super(MessageType.CONTEXT_ACTION_CHOSEN);
	this.chosen = chosen;
	this.chosenItem = chosenItem;
    }

    @Override
    public void processForServer(ServerContext context, Client client) {
	context.proccessActionInContext(client, chosenItem, chosen);
    }

}
