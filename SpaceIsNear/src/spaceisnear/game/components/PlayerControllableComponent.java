/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import spaceisnear.game.GamerPlayer;
import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessageControlled;
import spaceisnear.game.messages.MessageMoved;
import spaceisnear.game.messages.MessageTypes;

public class PlayerControllableComponent extends Component {

    @Override
    public void processMessage(Message message) {
	if (message.getMessageType() == MessageTypes.CONTROLLED) {
	    MessageControlled mc = (MessageControlled) message;
	    switch (mc.getType()) {
		case UP:
		    getOwner().message(new MessageMoved(0, -1));
		    break;
		case DOWN:
		    getOwner().message(new MessageMoved(0, 1));
		    break;
		case LEFT:
		    getOwner().message(new MessageMoved(-1, 0));
		    break;
		case RIGHT:
		    getOwner().message(new MessageMoved(1, 0));
		    break;
	    }
	}
    }
}
