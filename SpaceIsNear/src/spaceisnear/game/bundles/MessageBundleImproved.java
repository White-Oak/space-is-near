/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.bundles;

import spaceisnear.game.messages.MessageType;
import spaceisnear.game.objects.GameObjectState;

public class MessageBundleImproved extends MessageBundle {

    public GameObjectState state;

    public MessageBundleImproved(MessageType messageType) {
	super(messageType);
    }
}
