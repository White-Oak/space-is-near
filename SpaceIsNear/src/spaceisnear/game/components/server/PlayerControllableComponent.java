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

import java.util.List;
import spaceisnear.abstracts.AbstractGameObject;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentType;
import spaceisnear.game.messages.*;
import spaceisnear.game.objects.Position;
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
		MessageControlledByInput mc = (MessageControlledByInput) message;
		MessageMoved mm = null;
		Position position = getPosition();
		int oldX = position.getX();
		int oldY = position.getY();
		switch (mc.getType()) {
		    case UP:
			mm = moveCheck(oldX, oldY, 0, -1);
			break;
		    case DOWN:
			mm = moveCheck(oldX, oldY, 0, 1);
			break;
		    case LEFT:
			mm = moveCheck(oldX, oldY, -1, 0);
			break;
		    case RIGHT:
			mm = moveCheck(oldX, oldY, 1, 0);
			break;
		}
		if (mm != null) {
		    getOwner().message(mm);
//		    Context.LOG.log("So we say them to move");
		    getContext().sendDirectedMessage(new MessageToSend(mm));
		}
	}
    }

    private MessageMoved moveCheck(final int oldx, final int oldy, int deltaX, int deltaY) {
	int x = oldx + deltaX;
	int y = oldy + deltaY;
	ServerContext context = (ServerContext) getContext();
	MessageMoved mm = null;
	Object knockbacked = getStateValueNamed("knockbacked");
	if (knockbacked == null || (Boolean) knockbacked == false) {
	    if (context.getObstacles().isReacheable(x, y)) {
		//If cell is empty then simply move
		mm = new MessageMoved(deltaX, deltaY, getOwnerId());
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

    private MessageMoved pushOn(int x, int y, int deltaX, int deltaY) {
	final ServerContext context = (ServerContext) getContext();
	final List<AbstractGameObject> itemsOn = context.itemsOn(x, y);
	for (AbstractGameObject abstractGameObject : itemsOn) {
	    StaticItem staticItem = (StaticItem) abstractGameObject;
	    boolean blockingPath = checkIfBlocking(staticItem);
	    if (blockingPath) {
		Boolean property = (Boolean) staticItem.getVariableProperties().getProperty("stucked");
		if (property != null && !property) {
		    if (context.getObstacles().isReacheable(x + deltaX, y + deltaY)) {
			MessageMoved mm = new MessageMoved(deltaX, deltaY, staticItem.getId());
			staticItem.message(mm);
			getContext().sendDirectedMessage(new MessageToSend(mm));
			mm = new MessageMoved(deltaX, deltaY, getOwnerId());
			return mm;
		    }
		} else {
		    MessageInteracted interaction = new MessageInteracted(staticItem.getId(), -1);
		    context.sendDirectedMessage(interaction);
		}
	    }
	}
	return null;
    }

    private boolean checkIfBlocking(StaticItem staticItem) {
	boolean blockingPath = staticItem.getProperties().getBundle().blockingPath;
	String blockingPathString = (String) staticItem.getVariableProperties().getProperty("blockingPath");
	if (blockingPathString != null) {
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

		MessageMoved mm1 = new MessageMoved(oldx - positionToPull.getX(), oldy - positionToPull.getY(), get.getId());
		getContext().sendDirectedMessage(mm1);
		getContext().sendDirectedMessage(new MessageToSend(mm1));
	    } else {
		setStateValueNamed("pull", -1);
	    }
//		Context.LOG.log("pulled " + mm1.getX() + " " + mm1.getY());}
	}
    }
}
