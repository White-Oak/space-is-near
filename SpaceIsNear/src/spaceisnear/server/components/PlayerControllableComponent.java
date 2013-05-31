/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.server.components;

import spaceisnear.game.components.*;
import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessageControlled;
import spaceisnear.game.messages.MessageMoved;
import spaceisnear.game.messages.MessageType;

public class PlayerControllableComponent extends Component {

    @Override
    public void processMessage(Message message) {
	if (message.getMessageType() == MessageType.CONTROLLED) {
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
		getOwner().message(mm);
		getContext().getNetworking().send(mm);
	    }
	}
    }
}
