/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages.service;

import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessageType;
import spaceisnear.game.messages.NetworkableMessage;

public class MessageRogerRequested extends Message implements NetworkableMessage {

    public MessageRogerRequested() {
	super(MessageType.ROGER_REQUESTED);
    }

}
