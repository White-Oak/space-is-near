/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import spaceisnear.Context;
import spaceisnear.game.GameContext;
import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessageControlled;
import spaceisnear.game.messages.MessageMoved;
import spaceisnear.game.objects.GameObject;
import spaceisnear.game.objects.Position;

public class GamePlayerPositionComponent extends PositionComponent {

    public GamePlayerPositionComponent(Position p, int owner) {
	super(p);
	getStates().add(new ComponentState("owner", owner));
    }

    private GameObject getOwner() {
	return ((GameContext) getContext()).getObjects().get((Integer) getStateNamed("owner"));
    }

    @Override
    public void processMessage(Message message) {
	switch (message.getMessageType()) {
	    case MOVED:
		MessageMoved messagem = (MessageMoved) message;
		int newX = getX() + messagem.getX();
		int newY = getY() + messagem.getY();
		if (((Context) getContext()).getTiledLayer().getObstacles().isReacheable(newX, newY)) {
		    setX(newX);
		    setY(newY);
		    ((GameContext) getContext()).getCamera().setNewCameraPositionFor(messagem.getX(), messagem.getY());
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
//		    getOwner().message(mm);
		    ((GameContext) getContext()).getNetworking().send(mm);
		}
	}
    }

}
