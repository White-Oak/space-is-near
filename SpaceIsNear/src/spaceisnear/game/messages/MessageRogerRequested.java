/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

public class MessageRogerRequested extends Message implements NetworkableMessage {

    public MessageRogerRequested() {
	super(MessageType.ROGER_REQUESTED);
    }

}
