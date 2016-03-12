/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages.service;

import spaceisnear.game.messages.*;

/**
 *
 * @author White Oak
 */
public class MessageUnpaused extends Message implements NetworkableMessage {

    public MessageUnpaused() {
	super(MessageType.UNPAUSED);
    }
}
