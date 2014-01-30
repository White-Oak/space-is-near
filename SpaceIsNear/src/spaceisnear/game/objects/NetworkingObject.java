/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects;

import spaceisnear.game.GameContext;
import spaceisnear.game.components.client.NetworkingComponent;
import spaceisnear.game.messages.NetworkableMessage;

public class NetworkingObject extends ClientGameObject {

    public NetworkingObject(GameContext context) {
	super(GameObjectType.NETWORKING, context);
	addComponents(new NetworkingComponent());
    }

    public void send(NetworkableMessage m) {
	getContext().getNetworking().send(m);
    }
}
