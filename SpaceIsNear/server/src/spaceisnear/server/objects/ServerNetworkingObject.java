/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.server.objects;

import spaceisnear.game.components.server.ServerNetworkingComponent;
import spaceisnear.game.messages.NetworkableMessage;
import spaceisnear.game.objects.GameObjectType;
import spaceisnear.server.ServerContext;

public class ServerNetworkingObject extends ServerGameObject {

    public ServerNetworkingObject(ServerContext context) {
	super(GameObjectType.NETWORKING, context);
	addComponents(new ServerNetworkingComponent());
    }

    public void send(NetworkableMessage m) {
	((ServerContext) getContext()).getNetworking().sendToAll(m);
    }
}
