/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import lombok.Getter;
import spaceisnear.game.GameContext;
import spaceisnear.game.layer.AtmosphericLayer;
import spaceisnear.game.layer.ObstaclesLayer;
import spaceisnear.game.messages.*;
import spaceisnear.game.messages.properties.MessagePositionChanged;
import spaceisnear.game.objects.*;
import spaceisnear.server.ServerContext;
import spaceisnear.server.objects.items.StaticItem;

public class PositionComponent extends Component {

    boolean animation;
    @Getter private int delayX, delayY;
    private int timeAccumulated;
    private static final int TIME_NEEDED_TO_ANIMATE = 350;
    private static final int STEP = 8;

    public PositionComponent(Position p) {
	super(ComponentType.POSITION);
	addState("position", p);
    }

    public PositionComponent(int x, int y) {
	this(new Position(x, y));
    }

    private PositionComponent() {
	super(ComponentType.POSITION);
    }

    @Override
    public Position getPosition() {
	return (Position) getStateValueNamed("position");
    }

    public void setPosition(Position p) {
	setStateValueNamed("position", p);
    }

    public boolean isAnimated() {
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

    @Override
    public void processMessage(Message message) {
	int oldX = getX(), oldY = getY();
	switch (message.getMessageType()) {
	    case TELEPORTED:
		//Note that MessageTeleported is the subclass of MessageMoved
		//But MessageMoved is no longer used
		MessagePositionChanged messaget = (MessagePositionChanged) message;
		//here no check for obstacles
		if (isClient() && messaget.isActuallyMovedMessage(oldX, oldY)) {
		    registerForAnimation();
		    delayX = (messaget.getX() - oldX) * GameContext.TILE_WIDTH;
		    delayY = (messaget.getY() - oldY) * GameContext.TILE_HEIGHT;
		    animation = true;
		    timeAccumulated = 0;
		}
		setX(messaget.getX());
		setY(messaget.getY());
		break;
	    case ANIMATION_STEP://client-only message
		checkAnimation();
	}
	if (!isClient()) {
	    checkConsequnces(oldX, oldY);
	}
    }

    private void checkConsequnces(int oldX, int oldY) {
	ServerContext context = (ServerContext) getContext();
	//if moved
	if (oldX != getX() || oldY != getY()) {
	    //if object is item
	    if (getOwner().getType() == GameObjectType.ITEM) {
		checkConsequencesForItem(context, oldX, oldY);
	    }
	}
    }

    private void checkConsequencesForItem(ServerContext context, int oldX, int oldY) {
	StaticItem item = (StaticItem) getOwner();
	if (item.getProperties().isBlockingPath()) {
	    ObstaclesLayer obstacles = context.getObstacles();
	    obstacles.setReacheable(oldX, oldY, true);
	    obstacles.setReacheable(getX(), getY(), false);
	}
	if (item.getProperties().isBlockingAir()) {
	    AtmosphericLayer atmosphere = context.getAtmosphere();
	    atmosphere.setAirReacheable(oldX, oldY, true);
	    atmosphere.setAirReacheable(getX(), getY(), false);
	}
    }

    private void checkAnimation() {
	if (animation) {
	    int timePassed = MessageAnimationStep.STEP;
	    timeAccumulated += timePassed;
//		    if (timeAccumulated >= TIME_NEDEED_TO_MOVE_ON_TO_NEXT_PHASE_OF_ANIMATION) {
	    if (delayX != 0 || delayY != 0) {
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
	    }
	    if (timeAccumulated > TIME_NEEDED_TO_ANIMATE) {
		animation = false;
		unregisterForAnimation();
		timeAccumulated = 0;
		delayX = 0;
		delayY = 0;
	    }
	}
    }
}