/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessageMoved;
import spaceisnear.game.objects.Position;

public class GamePlayerPositionComponent extends PositionComponent {

    public GamePlayerPositionComponent(Position p) {
	super(p);
    }

    @Override
    public void processMessage(Message message) {
	int oldX = getX();
	int oldY = getY();
	super.processMessage(message);
	switch (message.getMessageType()) {
	    case MOVED:
		MessageMoved messagem = (MessageMoved) message;
		if (oldX != getX() || oldY != getY()) {
		    getContext().getCameraMan().setNewCameraPositionForMove(messagem.getX(), messagem.getY());
		}
		break;
	    case TELEPORTED:
		//Note that MessageTeleported is the subclass of MessageMoved
		MessageMoved messagetMessageMoved = (MessageMoved) message;
		getContext().getCameraMan().moveCameraToPlayer(messagetMessageMoved.getX(), messagetMessageMoved.getY());
		break;
	    case TIME_PASSED:
		
	}
    }

}
