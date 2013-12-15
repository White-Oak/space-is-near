/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessageToSend;
import spaceisnear.server.objects.ServerNetworkingObject;

public class ServerNetworkingComponent extends NetworkingComponent {

    @Override
    public void processMessage(Message message) {
	switch (message.getMessageType()) {
	    case NETWORKING:
		((ServerNetworkingObject) getOwner()).send(((MessageToSend) message).getMessage());
		break;
	}
    }

}
