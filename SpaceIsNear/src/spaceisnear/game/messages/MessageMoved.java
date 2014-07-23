package spaceisnear.game.messages;

import lombok.Getter;
import spaceisnear.game.GameContext;
import spaceisnear.game.objects.Position;
import spaceisnear.server.*;

/**
 * Sent only by server.
 *
 * @author White Oak
 */
class MessageMoved extends DirectedMessage implements NetworkableMessage {

    @Getter private final Position p;

    public MessageMoved(Position p, int id) {
	super(MessageType.MOVED, id);
	this.p = p;
    }

    public MessageMoved(int x, int y, int id) {
	this(new Position(x, y), id);
    }

    public int getX() {
	return p.getX();
    }

    public int getY() {
	return p.getY();
    }

    protected MessageMoved(Position p, int id, int unused) {
	super(MessageType.TELEPORTED, id);
	this.p = p;
    }

    @Override
    public String toString() {
	return "moved in" + p.toString() + " to " + id;
    }

    @Override
    public void processForServer(ServerContext context, Client client) {
	context.sendDirectedMessage(this);
	context.sendThemAll(this);
    }

    @Override
    public void processForClient(GameContext context) {
	context.sendDirectedMessage(this);
    }

}
