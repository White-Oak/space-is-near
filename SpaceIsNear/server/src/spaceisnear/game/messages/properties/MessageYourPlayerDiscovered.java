/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages.properties;

import lombok.*;
import spaceisnear.game.messages.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE) public class MessageYourPlayerDiscovered extends DirectedMessage implements NetworkableMessage, MessagePropertable {

    public MessageYourPlayerDiscovered(int playerID) {
	super(MessageType.DISCOVERED_PLAYER, playerID);
    }

}
