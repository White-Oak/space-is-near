/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.server.components;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessageMoved;
import spaceisnear.game.objects.Position;

@AllArgsConstructor @NoArgsConstructor public class PositionComponent extends Component {

    @Getter private int x, y;

    public PositionComponent(Position p) {
	this(p.getX(), p.getY());
    }

    @Override
    public void processMessage(Message message) {
	switch (message.getMessageType()) {
	    case MOVED:
		MessageMoved messagem = (MessageMoved) message;
		x += messagem.getX();
		y += messagem.getY();
//		getContext().getCamera().setNewCameraPositionFor(messagem.getX(), messagem.getY());
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
