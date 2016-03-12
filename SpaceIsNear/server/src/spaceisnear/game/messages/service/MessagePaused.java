/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages.service;

import spaceisnear.game.messages.*;

public class MessagePaused extends Message implements NetworkableMessage {

    public MessagePaused() {
	super(MessageType.PAUSED);
    }
}
