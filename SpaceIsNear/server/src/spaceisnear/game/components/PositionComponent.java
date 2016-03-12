/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import lombok.Getter;
import me.whiteoak.minlog.Log;
import spaceisnear.game.layer.AtmosphericLayer;
import spaceisnear.game.layer.ObstaclesLayer;
import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.properties.MessagePositionChanged;
import spaceisnear.game.messages.server.MessagePlayerChunkUpdated;
import spaceisnear.game.objects.GameObjectType;
import spaceisnear.game.ui.Position;
import spaceisnear.server.ServerContext;
import spaceisnear.server.chunks.Chunk;
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
		setX(messaget.getX());
		setY(messaget.getY());
		if (!isClient()) {
		    checkConsequnces(oldX, oldY);
		}
		break;
	}
    }

    private void checkConsequnces(int oldX, int oldY) {
	ServerContext context = (ServerContext) getContext();
	//if moved
	if (oldX != getX() || oldY != getY()) {
	    final GameObjectType type = getOwner().getType();
	    if (type == GameObjectType.ITEM) {
		//updating obstacle and atmospheric maps
		checkConsequencesForItem(context, oldX, oldY);
	    } else if (type == GameObjectType.PLAYER) {
		//updating chunk
		if (movedToAnotherChunk(oldX, oldY, getX(), getY())) {
		    Log.debug("server", "Player has moved to a new chunk, thus I'm sendidng this message.");
		    MessagePlayerChunkUpdated messagePlayerChunkUpdated = new MessagePlayerChunkUpdated(getOwnerId());
		    getContext().sendDirectedMessage(messagePlayerChunkUpdated);
		}
	    }

	}
    }

    private boolean movedToAnotherChunk(int oldX, int oldY, int newX, int newY) {
	oldX /= Chunk.CHUNK_SIZE;
	oldY /= Chunk.CHUNK_SIZE;
	newX /= Chunk.CHUNK_SIZE;
	newY /= Chunk.CHUNK_SIZE;
	return oldX != newX || oldY != newY;
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

}
