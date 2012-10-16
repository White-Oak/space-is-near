/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessageNetworkSended;
import spaceisnear.game.messages.MessageTypes;

public class NetworkSenderComponent extends Component {

    @Override
    public void processMessage(Message message) {
	if (message.getMessageType() == MessageTypes.ASKED_TO_SEND) {
	    MessageNetworkSended mns = (MessageNetworkSended) message;
//	    getContext().getNetworking().send(mns.getData());
	}
    }
}
