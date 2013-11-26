/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import lombok.Getter;

public class MessageToSend extends DirectedMessage {

    @Getter private final NetworkableMessage message;

    public MessageToSend(NetworkableMessage m) {
	super(MessageType.NETWORKING);
	message = m;
    }
}
