/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components.server;

import spaceisnear.game.components.client.NetworkingComponent;
import spaceisnear.game.messages.*;
import spaceisnear.game.messages.server.MessageChunkUpdated;
import spaceisnear.server.ServerContext;
import spaceisnear.server.objects.Player;
import spaceisnear.server.objects.ServerNetworkingObject;

public class ServerNetworkingComponent extends NetworkingComponent {

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
	    case CHUNK_UPDATED: {
		final MessageChunkUpdated mcu = (MessageChunkUpdated) message;
		int playerID = mcu.getPlayerID();
		ServerContext context = (ServerContext) getContext();
		context.getNetworking().playerChunkUpdated(playerID);
	    }
	}
    }

}
