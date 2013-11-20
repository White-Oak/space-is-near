/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import spaceisnear.game.bundles.Bundle;
import spaceisnear.game.bundles.NullBundle;

public class MessagePaused extends Message implements NetworkableMessage {

    public MessagePaused() {
	super(MessageType.PAUSED);
    }

    @Override
    public Bundle getBundle() {
	return new NullBundle();
    }
}
