/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages.service.onceused;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import spaceisnear.game.messages.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE) public class MessageWorldInformation extends Message implements NetworkableMessage {

    public int amountOfItems;
    public int propertyAmount;

    public MessageWorldInformation(int amountOfItems, int propertyAmount) {
	super(MessageType.WORLD_INFO);
	this.amountOfItems = amountOfItems;
	this.propertyAmount = propertyAmount;
    }

}
