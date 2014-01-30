/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components.server;

import spaceisnear.Context;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentType;
import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessageControlled;
import spaceisnear.game.messages.MessageMoved;
import spaceisnear.game.messages.MessageToSend;
import spaceisnear.game.objects.Position;
import spaceisnear.server.ServerContext;

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
			oldY--;
			if (context.getObstacles().isReacheable(oldX, oldY)) {
			    mm = new MessageMoved(0, -1, getOwnerId());
			}
			break;
		    case DOWN:
			oldY++;
			if (context.getObstacles().isReacheable(oldX, oldY)) {
			    mm = new MessageMoved(0, 1, getOwnerId());
			}
			break;
		    case LEFT:
			oldX--;
			if (context.getObstacles().isReacheable(oldX, oldY)) {
			    mm = new MessageMoved(-1, 0, getOwnerId());
			}
			break;
		    case RIGHT:
			oldX++;
			if (context.getObstacles().isReacheable(oldX, oldY)) {
			    mm = new MessageMoved(1, 0, getOwnerId());
			}
			break;
		}
		if (mm != null) {
		    getOwner().message(mm);
//		    System.out.println("So we say them to move");
		    getContext().sendDirectedMessage(new MessageToSend(mm));
		}
	}
    }

}
