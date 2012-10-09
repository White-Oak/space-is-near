/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessageMoved;
import spaceisnear.game.messages.MessageNetworkReceived;
import spaceisnear.game.messages.MessageTypes;

public class NetworkControllableComponent extends Component {

    @Override
    public void processMessage(Message message) {
	if (message.getMessageType() == MessageTypes.NETWORK_RECEIVED) {
	    MessageNetworkReceived mnr = (MessageNetworkReceived) message;
	    if (mnr.getObject() instanceof MessageMoved) {
		MessageMoved mm = (MessageMoved) mnr.getObject();
		getOwner().message(mm);
	    }
	}
    }
}
