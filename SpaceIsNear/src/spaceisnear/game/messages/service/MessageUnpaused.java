/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages.service;

import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessageType;
import spaceisnear.game.messages.NetworkableMessage;

/**
 *
 * @author White Oak
 */
public class MessageUnpaused extends Message implements NetworkableMessage {

    public MessageUnpaused() {
	super(MessageType.UNPAUSED);
    }
}
