/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages.service;

import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessageType;
import spaceisnear.game.messages.NetworkableMessage;

public class MessageRogered extends Message implements NetworkableMessage {

    public MessageRogered() {
	super(MessageType.ROGERED);
    }
}
