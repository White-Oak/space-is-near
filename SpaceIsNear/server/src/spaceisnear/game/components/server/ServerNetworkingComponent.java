/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components.server;

import spaceisnear.abstracts.Context;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentType;
import spaceisnear.game.messages.*;
import spaceisnear.server.ServerContext;
import spaceisnear.server.objects.Player;
import spaceisnear.server.objects.ServerNetworkingObject;

public class ServerNetworkingComponent extends Component {

    public ServerNetworkingComponent() {
	super(ComponentType.NETWORKING);
	setOwnerId(Context.NETWORKING_ID);
    }
    @Override
    public void processMessage(Message message) {
	switch (message.getMessageType()) {
	    case NETWORKING:
		final MessageToSend mts = (MessageToSend) message;
		if (mts.isBroadcast()) {
		    ((ServerNetworkingObject) getOwner()).send(mts.getMessage());
		} else {
		    ServerContext context = (ServerContext) getContext();
		    final DirectedMessage message1 = (DirectedMessage) mts.getMessage();
		    Player get = (Player) context.getObjects().get(message1.getId());
		    context.getNetworking().sendToPlayer(get, mts.getMessage());
		}
		break;
	}
    }

}
