/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Sent by server when knocked by atmosphere.
 *
 * @author White Oak
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE) public class MessageKnockbacked extends DirectedMessage implements NetworkableMessage {

    public MessageKnockbacked(int id) {
	super(MessageType.KNOCKBACKED, id);
    }

}
