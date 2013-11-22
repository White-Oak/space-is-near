/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

public class MessageConnectionBroken extends Message implements NetworkableMessage {

    public MessageConnectionBroken() {
	super(MessageType.BROKEN);
    }
}
