package spaceisnear.game.messages;

import spaceisnear.game.messages.properties.MessagePropertable;
import spaceisnear.game.objects.Position;

public class MessageTeleported extends MessageMoved implements MessagePropertable {

    public MessageTeleported(Position p, int id) {
	super(p, id, 0);
    }

    public MessageTeleported(Position old, int id, Position moved) {
	super(new Position(old.getX() + moved.getX(), old.getY() + moved.getY()), id, 0);
    }

    public boolean isActuallyMovedMessage(int oldX, int oldY) {
//	return ((getX() <= 1 && getX() >= -1) || (getY() <= 1 && getY() >= -1));
	int dx = Math.abs(oldX - getX());
	int dy = Math.abs(oldY - getY());
	return dx == 1 || dy == 1;
    }
}
