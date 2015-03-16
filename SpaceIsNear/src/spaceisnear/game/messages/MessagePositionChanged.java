package spaceisnear.game.messages;

import lombok.Getter;
import spaceisnear.game.GameContext;
import spaceisnear.game.messages.properties.MessagePropertable;
import spaceisnear.game.objects.Position;
import spaceisnear.server.Client;
import spaceisnear.server.ServerContext;

public class MessagePositionChanged extends DirectedMessage implements MessagePropertable {

    @Getter private final Position p;

    public MessagePositionChanged(Position p, int id) {
	super(MessageType.TELEPORTED, id);
	this.p = p;
    }

    public int getX() {
	return p.getX();
    }

    public int getY() {
	return p.getY();
    }

    public MessagePositionChanged(Position old, int id, Position moved) {
	this(new Position(old.getX() + moved.getX(), old.getY() + moved.getY()), id);
    }

    @Override
    public String toString() {
	return "moved to " + p.toString() + " to " + id;
    }

    public boolean isActuallyMovedMessage(int oldX, int oldY) {
	int dx = Math.abs(oldX - getX());
	int dy = Math.abs(oldY - getY());
	return dx == 1 || dy == 1;
    }

    @Override
    public void processForServer(ServerContext context, Client client) {
	context.sendDirectedMessage(this);
//	context.sendThemAll(this);
    }

    @Override
    public void processForClient(GameContext context) {
	context.sendDirectedMessage(this);
    }
}
