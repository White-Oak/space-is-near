/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components.client;

import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentType;
import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.service.MessageNetworkState;

public class UIComponent extends Component {

    public UIComponent() {
	super(ComponentType.UI);
    }

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
