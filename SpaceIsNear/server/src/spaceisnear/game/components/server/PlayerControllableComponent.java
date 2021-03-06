/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components.server;

import spaceisnear.game.components.VariablePropertiesComponent;
import java.util.List;
import spaceisnear.abstracts.AbstractGameObject;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentType;
import spaceisnear.game.messages.*;
import spaceisnear.game.messages.properties.MessagePositionChanged;
import spaceisnear.game.ui.Position;
import spaceisnear.server.ServerContext;
import spaceisnear.server.objects.items.StaticItem;

public class PlayerControllableComponent extends Component {

    public PlayerControllableComponent() {
	super(ComponentType.PLAYER_CONTROLLABLE);
    }

    @Override
    public void processMessage(Message message) {
	switch (message.getMessageType()) {
	    case CONTROLLED:
		final MessageControlledByInput mc = (MessageControlledByInput) message;
		Position newPosition = null;
		final Position position = getPosition();
		final int oldX = position.getX();
		final int oldY = position.getY();
		switch (mc.getType()) {
		    case UP:
			newPosition = moveCheck(oldX, oldY, 0, -1);
			break;
		    case DOWN:
			newPosition = moveCheck(oldX, oldY, 0, 1);
			break;
		    case LEFT:
			newPosition = moveCheck(oldX, oldY, -1, 0);
			break;
		    case RIGHT:
			newPosition = moveCheck(oldX, oldY, 1, 0);
			break;
		}
		if (newPosition != null) {
		    final MessagePositionChanged messageTeleported;
		    messageTeleported = new MessagePositionChanged(position, getOwnerId(), newPosition);
		    getOwner().message(messageTeleported);
		    getContext().sendToNetwork(messageTeleported);
		}
	}
    }

    private Position moveCheck(final int oldx, final int oldy, int deltaX, int deltaY) {
	int x = oldx + deltaX;
	int y = oldy + deltaY;
	ServerContext context = (ServerContext) getContext();
	Position mm = null;
	Object knockbacked = getStateValueNamed("knockbacked");
	if (knockbacked == null || (Boolean) knockbacked == false) {
	    if (context.getObstacles().isReacheable(x, y)) {
		//If cell is empty then simply move
		mm = new Position(deltaX, deltaY);
	    } else if (context.isOnMap(x, y)) {
		//else try to push item on that cell
		mm = pushOn(x, y, deltaX, deltaY);
	    }
	}
	if (mm != null) {
	    checkPull(oldx, oldy);
	}
	return mm;
    }

    private Position pushOn(int x, int y, int deltaX, int deltaY) {
	final ServerContext context = (ServerContext) getContext();
	final List<AbstractGameObject> itemsOn = context.itemsOn(x, y);
	for (AbstractGameObject abstractGameObject : itemsOn) {
	    StaticItem staticItem = (StaticItem) abstractGameObject;
	    boolean blockingPath = checkIfBlocking(staticItem);
	    if (blockingPath) {
		Object prop = staticItem.getVariableProperties().getProperty("stucked");
		if (prop != null) {
		    boolean property = (boolean) prop;
		    if (!property) {
			if (context.getObstacles().isReacheable(x + deltaX, y + deltaY)) {
			    final MessagePositionChanged messageTeleported = new MessagePositionChanged(staticItem.getPosition(),
				    staticItem.getId(),
				    new Position(deltaX, deltaY));
			    staticItem.message(messageTeleported);
			    getContext().sendDirectedMessage(new MessageToSend(messageTeleported));
			    return new Position(deltaX, deltaY);
			}
		    } else {
			MessageInteracted interaction = new MessageInteracted(staticItem.getId(), getOwnerId());
			context.sendDirectedMessage(interaction);
		    }
		}
	    }
	}
	return null;
    }

    private boolean checkIfBlocking(StaticItem staticItem) {
	boolean blockingPath = staticItem.getProperties().getBundle().blockingPath;
	Object property = staticItem.getVariableProperties().getProperty("blockingPath");
	if (property != null) {
	    String blockingPathString = (String) property;
	    blockingPath = Boolean.parseBoolean(blockingPathString);
	}
	return blockingPath;
    }

    private void checkPull(final int oldx, final int oldy) {
	VariablePropertiesComponent variablePropertiesComponent = getOwner().getVariablePropertiesComponent();
	final Object pulled = variablePropertiesComponent.getProperty("pull");
	if (pulled != null && ((Integer) pulled) > -1) {
	    int toPull = (Integer) pulled;
	    AbstractGameObject get = getContext().getObjects().get(toPull);
	    Position positionToPull = get.getPosition();
	    Position position = getPosition();
	    if (Math.abs(positionToPull.getX() - position.getX()) <= 1 && Math.abs(
		    positionToPull.getY() - position.getY()) <= 1) {

		Position mm1 = new Position(oldx - positionToPull.getX(), oldy - positionToPull.getY());
		MessagePositionChanged messageTeleported = new MessagePositionChanged(get.getPosition(), get.getId(), mm1);
		getContext().sendDirectedMessage(messageTeleported);
		getContext().sendDirectedMessage(new MessageToSend(messageTeleported));
	    } else {
		setStateValueNamed("pull", -1);
	    }
//		Context.LOG.log("pulled " + mm1.getX() + " " + mm1.getY());}
	}
    }
}
