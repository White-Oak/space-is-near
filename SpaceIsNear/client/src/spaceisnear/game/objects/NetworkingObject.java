/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects;

import spaceisnear.game.Networking;
import spaceisnear.game.components.client.NetworkingComponent;
import spaceisnear.game.messages.NetworkableMessage;

public class NetworkingObject extends ClientGameObject {

    private final Networking networking;

    public NetworkingObject(Networking networking) {
	super(GameObjectType.NETWORKING);
	this.networking = networking;
	addComponents(new NetworkingComponent());
    }

    public void send(NetworkableMessage m) {
	networking.send(m);
    }
}
