/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components.client;

import spaceisnear.abstracts.Context;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentType;
import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessageToSend;
import spaceisnear.game.objects.NetworkingObject;

public class NetworkingComponent extends Component {

    public NetworkingComponent() {
	super(ComponentType.NETWORKING);
	setOwnerId(Context.NETWORKING_ID);
    }

    @Override
    public void processMessage(Message message) {
	switch (message.getMessageType()) {
	    case NETWORKING:
		((NetworkingObject) getOwner()).send(((MessageToSend) message).getMessage());
		break;
	}
    }

}
