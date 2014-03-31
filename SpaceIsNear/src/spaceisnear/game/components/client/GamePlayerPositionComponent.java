/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components.client;

import spaceisnear.game.GameContext;
import spaceisnear.game.components.PositionComponent;
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
	final GameContext context = (GameContext) getContext();
	switch (message.getMessageType()) {
	    case MOVED: {
		MessageMoved messagem = (MessageMoved) message;
		if (oldX != getX() || oldY != getY()) {
		    context.getCameraMan().moveCamera(messagem.getP().getX(), messagem.getP().getY());
		}
		break;
	    }
	    case TELEPORTED: {
		MessageMoved messagem = (MessageMoved) message;
		if (oldX != getX() || oldY != getY()) {
		    context.getCameraMan().moveCameraToPlayer(messagem.getP().getX(), messagem.getP().getY());
		}
		break;
	    }
	    case TIME_PASSED:
		break;
	}
    }

}
