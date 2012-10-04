/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessageNetworkState;

public class UIComponent extends Component {

    @Override
    public void processMessage(Message message) {
	switch (message.getMessageType()) {
	    case NETWORK_STATE:
		MessageNetworkState mns = (MessageNetworkState) message;
		System.out.println(mns.getState());
		break;
	}
    }
}
