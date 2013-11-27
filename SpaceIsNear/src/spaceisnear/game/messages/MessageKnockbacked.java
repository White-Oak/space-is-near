/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

/**
 * Sent by server when knocked by atmosphere.
 *
 * @author White Oak
 */
public class MessageKnockbacked extends DirectedMessage implements NetworkableMessage {

    public MessageKnockbacked() {
	super(MessageType.KNOCKBACKED);
    }

}
