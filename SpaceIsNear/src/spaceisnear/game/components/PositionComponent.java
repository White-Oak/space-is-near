/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import lombok.Getter;
import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessageMoved;

public class PositionComponent extends Component {

    @Getter private int x, y;

    @Override
    public void processMessage(Message message) {
	switch (message.getMessageType()) {
	    case MOVED:
		MessageMoved messagem = (MessageMoved) message;
		x += messagem.getX();
		y += messagem.getY();
		getContext().getCamera().setNewCameraPositionFor(x, y);
		break;
	    case TELEPORTED:
		//Note that MessageTeleported is the subclass of MessageMoved
		MessageMoved messagetMessageMoved = (MessageMoved) message;
		x = messagetMessageMoved.getX();
		y = messagetMessageMoved.getY();
		break;
	}
    }
}
