package spaceisnear.game.messages;

import lombok.Getter;
import spaceisnear.Utils;
import spaceisnear.server.*;

/**
 * @author LPzhelud
 */
public class MessageControlledByInput extends DirectedMessage implements NetworkableMessage {

    @Getter private final Type type;

    public MessageControlledByInput(Type type, int id) {
	super(MessageType.CONTROLLED, id);
	this.type = type;
    }

    public static enum Type {

	UP,
	DOWN,
	LEFT,
	RIGHT;

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
