/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages.service.onceused;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import spaceisnear.game.GameContext;
import spaceisnear.game.messages.*;
import spaceisnear.starting.LoadingScreen;

@NoArgsConstructor(access = AccessLevel.PRIVATE) public class MessageWorldInformation extends Message implements NetworkableMessage {

    public int amountOfItems;
    public int propertyAmount;

    public MessageWorldInformation(int amountOfItems, int propertyAmount) {
	super(MessageType.WORLD_INFO);
	this.amountOfItems = amountOfItems;
	this.propertyAmount = propertyAmount;
    }

    @Override
    public void processForClient(GameContext context) {
	LoadingScreen.LOADING_AMOUNT = amountOfItems;
	LoadingScreen.CURRENT_AMOUNT = 0;
    }

}
