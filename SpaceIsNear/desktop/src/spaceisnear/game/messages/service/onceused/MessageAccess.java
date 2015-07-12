package spaceisnear.game.messages.service.onceused;

import lombok.*;
import spaceisnear.game.GameContext;
import spaceisnear.game.Networking;
import spaceisnear.game.messages.*;
import spaceisnear.game.ui.console.ChatString;
import spaceisnear.game.ui.console.LogLevel;

@NoArgsConstructor(access = AccessLevel.PRIVATE) public class MessageAccess extends Message implements NetworkableMessage {

    @Getter private boolean access;

    public MessageAccess(boolean access) {
	super(MessageType.ACCESS);
	this.access = access;
    }

    @Override
    public void processForClient(GameContext context) {
	final Networking networking = context.getEngine().getNetworking();
	networking.setLogined(isAccess());
	if (!networking.isLogined()) {
	    context.getEngine().chat(new ChatString("Incorrect pair of login/password", LogLevel.WARNING));
	}
    }

}
