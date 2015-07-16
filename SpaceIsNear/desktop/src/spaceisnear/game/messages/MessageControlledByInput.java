package spaceisnear.game.messages;

import lombok.*;
import spaceisnear.Utils;
import spaceisnear.server.*;

/**
 * @author LPzhelud
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE) public class MessageControlledByInput extends DirectedMessage implements NetworkableMessage {

    @Getter private Type type;

    public MessageControlledByInput(Type type, int id) {
	super(MessageType.CONTROLLED, id);
	this.type = type;
    }

    public static enum Type {

	UP,
	DOWN,
	LEFT,
	RIGHT,
	UPLEFT, UPRIGHT,
	DOWNLEFT, DOWNRIGHT

    }

    public static MessageControlledByInput getInstance(byte[] b) {
	return Utils.GSON.fromJson(new String(b), MessageControlledByInput.class);
    }

    @Override
    public String toString() {
	return "Controlled " + type;
    }

    @Override
    public void processForServer(ServerContext context, Client client) {
	context.sendDirectedMessage(this);
    }

}
