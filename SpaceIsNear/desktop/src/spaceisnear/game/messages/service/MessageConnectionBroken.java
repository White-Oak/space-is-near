/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages.service;

import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessageType;
import spaceisnear.game.messages.NetworkableMessage;

/**
 * Not used yet.
 *
 * @author White Oak
 */
public class MessageConnectionBroken extends Message implements NetworkableMessage {

    public MessageConnectionBroken() {
	super(MessageType.BROKEN);
    }
}
