/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import spaceisnear.game.bundles.Bundle;
import spaceisnear.game.bundles.NullBundle;

public class MessageConnectionBroken extends Message implements NetworkableMessage {

    public MessageConnectionBroken() {
	super(MessageType.BROKEN);
    }

    @Override
    public Bundle getBundle() {
	return new NullBundle();
    }
}
