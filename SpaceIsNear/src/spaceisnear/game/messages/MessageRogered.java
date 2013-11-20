/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import spaceisnear.game.bundles.Bundle;
import spaceisnear.game.bundles.NullBundle;

public class MessageRogered extends Message implements NetworkableMessage {

    public MessageRogered() {
	super(MessageType.ROGERED);
    }

    @Override
    public Bundle getBundle() {
	return new NullBundle();
    }
}
