package spaceisnear.game.messages;

import lombok.*;
import spaceisnear.game.GameContext;
import spaceisnear.game.components.server.context.ServerContextMenu;

/**
 *
 * @author White Oak
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE) public class MessageActionsOffer extends Message implements NetworkableMessage {

    @Getter private ServerContextMenu menu;

    public MessageActionsOffer(ServerContextMenu menu) {
	super(MessageType.CONTEXT_ACTIONS_OFFER);
	this.menu = menu;
    }

    @Override
    public void processForClient(GameContext context) {
	context.updateCurrentMenu(menu);
    }

}
