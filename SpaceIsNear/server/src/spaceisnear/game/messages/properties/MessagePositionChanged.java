package spaceisnear.game.messages.properties;

import lombok.*;
import spaceisnear.game.messages.DirectedMessage;
import spaceisnear.game.messages.MessageType;
import spaceisnear.game.ui.Position;

@NoArgsConstructor(access = AccessLevel.PRIVATE) public class MessagePositionChanged extends DirectedMessage implements MessagePropertable {

    @Getter private Position p;

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
	return getId() + " moved to " + p.toString();
    }

    public boolean isActuallyMovedMessage(int oldX, int oldY) {
	int dx = Math.abs(oldX - getX());
	int dy = Math.abs(oldY - getY());
	return dx == 1 || dy == 1;
    }

}
