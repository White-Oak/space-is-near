/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE) public class MessageDied extends DirectedMessage implements NetworkableMessage {

    public MessageDied(int id) {
	super(MessageType.DIED, id);
    }
}
