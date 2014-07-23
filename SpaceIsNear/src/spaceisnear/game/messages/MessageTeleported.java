package spaceisnear.game.messages;

import spaceisnear.game.GameContext;
import spaceisnear.game.messages.properties.MessagePropertable;
import spaceisnear.game.objects.Position;

public class MessageTeleported extends MessageMoved implements MessagePropertable {

    public MessageTeleported(Position p, int id) {
	super(p, id, 0);
    }

    public MessageTeleported(Position p, int id, Position moved) {
	super(new Position(p.getX() + moved.getX(), p.getY() + moved.getY()), id, 0);
    }

    @Override
    public void processForClient(GameContext context) {
	context.sendDirectedMessage(this);
    }

    public boolean seemsLikeMoved() {
	return ((getX() <= 1 && getX() >= -1) || (getY() <= 1 && getY() >= -1));
    }
}
