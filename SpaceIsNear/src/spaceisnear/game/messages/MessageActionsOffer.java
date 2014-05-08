package spaceisnear.game.messages;

import spaceisnear.game.GameContext;
import spaceisnear.game.components.server.scriptprocessors.context.ServerContextMenu;

/**
 *
 * @author White Oak
 */
public class MessageActionsOffer extends Message implements NetworkableMessage {

    private final ServerContextMenu menu;

    public MessageActionsOffer(ServerContextMenu menu) {
	super(MessageType.CONTEXT_ACTIONS_OFFER);
	this.menu = menu;
    }

    @Override
    public void processForClient(GameContext context) {
    }

}
