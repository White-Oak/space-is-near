package spaceisnear.game.messages.service.onceused;

import lombok.Getter;
import spaceisnear.game.GameContext;
import spaceisnear.game.messages.*;
import spaceisnear.game.ui.console.LogLevel;
import spaceisnear.game.ui.console.LogString;

public class MessageAccess extends Message implements NetworkableMessage {

    @Getter private final boolean access;

    public MessageAccess(boolean access) {
	super(MessageType.ACCESS);
	this.access = access;
    }

    @Override
    public void processForClient(GameContext context) {
	context.getCore().getNetworking().setLogined(isAccess());
	if (!context.getCore().getNetworking().isLogined()) {
	    context.getCore().log(new LogString("Incorrect pair of login/password", LogLevel.WARNING));
	}
    }

}
