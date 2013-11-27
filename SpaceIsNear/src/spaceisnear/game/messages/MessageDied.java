/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

public class MessageDied extends DirectedMessage implements NetworkableMessage {

    public MessageDied() {
	super(MessageType.DIED);
    }
}
