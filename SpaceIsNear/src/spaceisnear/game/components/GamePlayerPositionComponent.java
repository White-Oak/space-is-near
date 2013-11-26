/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import spaceisnear.Context;
import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessageControlled;
import spaceisnear.game.messages.MessageMoved;
import spaceisnear.game.messages.MessageToSend;
import spaceisnear.game.objects.Position;

public class GamePlayerPositionComponent extends PositionComponent {

    public GamePlayerPositionComponent(Position p, int owner) {
	super(p);
	getStates().add(new ComponentState("owner", owner));
    }

    @Override
    public void processMessage(Message message) {
	switch (message.getMessageType()) {
	    case MOVED:
		MessageMoved messagem = (MessageMoved) message;
		int newX = getX() + messagem.getX();
		int newY = getY() + messagem.getY();
		if (((Context) getContext()).getCameraMan().getObstacles().isReacheable(newX, newY)) {
		    setX(newX);
		    setY(newY);
		    ((Context) getContext()).getCameraMan().setNewCameraPositionFor(messagem.getX(), messagem.getY());
		}
		break;
	    case TELEPORTED:
		//Note that MessageTeleported is the subclass of MessageMoved
		MessageMoved messagetMessageMoved = (MessageMoved) message;
		//here no check for obstacles
		setX(messagetMessageMoved.getX());
		setY(messagetMessageMoved.getY());
		break;
	    case CONTROLLED:
		MessageControlled mc = (MessageControlled) message;
		MessageMoved mm = null;
		switch (mc.getType()) {
		    case UP:
			mm = new MessageMoved(0, -1, getOwner().getId());
			break;
		    case DOWN:
			mm = new MessageMoved(0, 1, getOwner().getId());
			break;
		    case LEFT:
			mm = new MessageMoved(-1, 0, getOwner().getId());
			break;
		    case RIGHT:
			mm = new MessageMoved(1, 0, getOwner().getId());
			break;
		}
		if (mm != null) {
		    getContext().sendToID(new MessageToSend(mm), Context.NETWORKING_ID);
		}
	}
    }

}
