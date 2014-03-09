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
import spaceisnear.server.objects.items.ServerItemsArchive;
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
	if (context.getObstacles().isReacheable(x, y)) {
	    //If cell is empty then simply move
	    mm = new MessageMoved(deltaX, deltaY, getOwnerId());
	} else if (context.isOnMap(x, y)) {
	    //else try to push item on that cell
	    List<AbstractGameObject> itemsOn = context.itemsOn(x, y);
	    for (AbstractGameObject abstractGameObject : itemsOn) {
		StaticItem staticItem = (StaticItem) abstractGameObject;
		int id = staticItem.getProperties().getId();
		boolean blockingPath = ServerItemsArchive.ITEMS_ARCHIVE.isBlockingPath(id);
		if (blockingPath) {
		    Boolean property = (Boolean) staticItem.getVariableProperties().getProperty("stucked");
		    if (property != null && !property && context.getObstacles().isReacheable(x + deltaX, y + deltaY)) {
			mm = new MessageMoved(deltaX, deltaY, staticItem.getId());
			staticItem.message(mm);
			getContext().sendDirectedMessage(new MessageToSend(mm));
			mm = new MessageMoved(deltaX, deltaY, getOwnerId());
			break;
		    }
		}
	    }
	}
	if (mm != null) {
	    checkPull(oldx, oldy);
	}
	return mm;
    }

    private void checkPull(final int oldx, final int oldy) {
	VariablePropertiesComponent variablePropertiesComponent = getOwner().getVariablePropertiesComponent();
	if (variablePropertiesComponent.getProperty("pull") != null && ((Integer) variablePropertiesComponent.getProperty("pull")) > -1) {
	    int toPull = (Integer) variablePropertiesComponent.getProperty("pull");
	    AbstractGameObject get = getContext().getObjects().get(toPull);
	    Position positionToPull = get.getPosition();
	    MessageMoved mm1 = new MessageMoved(oldx - positionToPull.getX(), oldy - positionToPull.getY(), get.getId());
	    getContext().sendDirectedMessage(mm1);
	    getContext().sendDirectedMessage(new MessageToSend(mm1));
//		Context.LOG.log("pulled " + mm1.getX() + " " + mm1.getY());}
	}
    }
}
