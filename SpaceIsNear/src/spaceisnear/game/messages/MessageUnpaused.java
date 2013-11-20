/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import spaceisnear.game.bundles.Bundle;
import spaceisnear.game.bundles.NullBundle;

/**
 *
 * @author White Oak
 */
public class MessageUnpaused extends Message implements NetworkableMessage {

    public MessageUnpaused() {
	super(MessageType.UNPAUSED);
    }

    @Override
    public Bundle getBundle() {
	return new NullBundle();
    }
}
