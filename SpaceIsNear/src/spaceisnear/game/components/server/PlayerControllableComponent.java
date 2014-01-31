/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components.server;

import java.util.List;
import spaceisnear.AbstractGameObject;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentType;
import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessageControlled;
import spaceisnear.game.messages.MessageMoved;
import spaceisnear.game.messages.MessageToSend;
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
		MessageControlled mc = (MessageControlled) message;
		MessageMoved mm = null;
		Position position = getPosition();
		int oldX = position.getX();
		int oldY = position.getY();
		final ServerContext context = (ServerContext) getContext();
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
//		    System.out.println("So we say them to move");
		    getContext().sendDirectedMessage(new MessageToSend(mm));
		}
	}
    }

    private MessageMoved moveCheck(int x, int y, int deltaX, int deltaY) {
	x += deltaX;
	y += deltaY;
	ServerContext context = (ServerContext) getContext();
	MessageMoved mm = null;
	if (context.getObstacles().isReacheable(x, y)) {
	    mm = new MessageMoved(deltaX, deltaY, getOwnerId());
	} else if (context.isOnMap(x, y)) {
	    List<AbstractGameObject> itemsOn = context.itemsOn(x, y);
	    for (AbstractGameObject abstractGameObject : itemsOn) {
		StaticItem staticItem = (StaticItem) abstractGameObject;
		int id = staticItem.getProperties().getId();
		boolean blockingPath = ServerItemsArchive.itemsArchive.isBlockingPath(id);
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
	return mm;
    }

}
