/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import spaceisnear.game.bundles.Bundle;
import spaceisnear.game.bundles.JSONBundle;
import spaceisnear.game.objects.GameObjectState;
import spaceisnear.game.objects.GameObjectType;

public class MessageCreated extends Message implements NetworkableMessage {

    private final String json;

    public MessageCreated(String json) {
	super(MessageType.CREATED);
	this.json = json;
    }

    @Override
    public Bundle getBundle() {
	return new JSONBundle(json);
    }
}
