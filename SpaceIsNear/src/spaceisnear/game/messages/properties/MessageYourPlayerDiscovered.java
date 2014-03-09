/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages.properties;

import lombok.Getter;
import spaceisnear.game.messages.*;

public class MessageYourPlayerDiscovered extends Message implements NetworkableMessage, MessagePropertable {

    @Getter private final int playerID;

    public MessageYourPlayerDiscovered(int playerID) {
	super(MessageType.DISCOVERED_PLAYER);
	this.playerID = playerID;
    }

}
