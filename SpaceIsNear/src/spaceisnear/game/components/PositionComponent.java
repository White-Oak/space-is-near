/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import spaceisnear.game.GameContext;
import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessageMoved;
import spaceisnear.game.messages.MessageTimePassed;
import spaceisnear.game.objects.Position;

public class PositionComponent extends Component {

    private Position p;
    boolean animation;
    private int delayX, delayY;
    private int timeAccumulated;
    private static final long TIME_NEDEED_TO_MOVE_ON_TO_NEXT_PHASE_OF_ANIMATION = 4L;
    private static final int STEP = 8;

    public PositionComponent(Position p) {
	super(ComponentType.POSITION);
	addState(new ComponentState("position", p));
    }

    public PositionComponent(int x, int y) {
	this(new Position(x, y));
    }

    private PositionComponent() {
	super(ComponentType.POSITION);
    }

    @Override
    public Position getPosition() {
	if (p == null) {
	    p = (Position) getStateValueNamed("position");
	}
	return p;
    }

    public boolean isAnimation() {
	return animation;
    }

    public int getX() {
	return getPosition().getX();
    }

    public int getY() {
	return getPosition().getY();
    }

    public void setX(int x) {
	getPosition().setX(x);
    }

    public void setY(int y) {
	getPosition().setY(y);
    }

    public int getDelayX() {
	return delayX;
    }

    public int getDelayY() {
	return delayY;
    }

    @Override
    public void processMessage(Message message) {
	switch (message.getMessageType()) {
	    case MOVED:
		MessageMoved messagem = (MessageMoved) message;
		int newX = getX() + messagem.getX();
		int newY = getY() + messagem.getY();
		if (getContext().getObstacles().isReacheable(newX, newY)) {
		    delayX = messagem.getX() * GameContext.TILE_WIDTH;
		    delayY = messagem.getY() * GameContext.TILE_HEIGHT;
		    animation = true;
		    setX(newX);
		    setY(newY);
		}
		break;
	    case TELEPORTED:
		//Note that MessageTeleported is the subclass of MessageMoved
		MessageMoved messagetMessageMoved = (MessageMoved) message;
		//here no check for obstacles
		setX(messagetMessageMoved.getX());
		setY(messagetMessageMoved.getY());
		break;
	    case TIME_PASSED:
		if (animation) {
//		    MessageTimePassed mtp = (MessageTimePassed) message;
//		    int timePassed = mtp.getTimePassed();
//		    timeAccumulated += timePassed;
//		    if (timeAccumulated >= TIME_NEDEED_TO_MOVE_ON_TO_NEXT_PHASE_OF_ANIMATION) {
		    if (true) {
			int step = STEP;
			if (delayX != 0) {
			    if (Math.abs(delayX) != delayX) {
				step = -step;
			    }
			    delayX -= step;
			}
			if (delayY != 0) {
			    if (Math.abs(delayY) != delayY) {
				step = -step;
			    }
			    delayY -= step;
			}
			if (delayX == 0 && delayY == 0) {
			    animation = false;
			}
		    }
		}
	}
    }
}
