/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import spaceisnear.game.bundles.Bundle;
import spaceisnear.game.objects.GameObjectState;
import spaceisnear.game.objects.GameObjectType;

public class MessageCreated extends Message implements NetworkableMessage {

    private final GameObjectState gameObjectState;

    public MessageCreated(GameObjectState gameObjectState) {
	super(MessageType.CREATED);
	this.gameObjectState = gameObjectState;
    }

    @Override
    public Bundle getBundle() {
	return gameObjectState.getBundle();
    }
}
