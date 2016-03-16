package spaceisnear.game.messages.service.onceused;

import me.whiteoak.minlog.Log;
import spaceisnear.game.GameContext;
import spaceisnear.game.messages.*;

public class MessageJoined extends Message implements NetworkableMessage {

    public MessageJoined() {
	super(MessageType.JOINED);
    }

    @Override
    public void processForClient(GameContext context) {
	context.getEngine().getNetworking().setJoined(true);
	Log.info("client", "Server told me to join");
    }

}
