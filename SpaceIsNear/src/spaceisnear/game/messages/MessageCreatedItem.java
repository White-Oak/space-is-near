/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import lombok.Getter;
import spaceisnear.game.GameContext;
import spaceisnear.game.objects.GameObjectType;
import spaceisnear.game.objects.items.ItemsArchive;
import spaceisnear.game.objects.items.StaticItem;
import spaceisnear.starting.LoadingScreen;

public class MessageCreatedItem extends MessageCreated {

    @Getter private final int id;

    public MessageCreatedItem(int id) {
	super(GameObjectType.ITEM, MessageType.CREATED_SIMPLIFIED_ITEM);
	this.id = id;
    }

    @Override
    public void processForClient(GameContext context) {
	StaticItem item = ItemsArchive.itemsArchive.getNewItem(getId());
	if (item != null) {
	    context.addObject(item);
	    LoadingScreen.CURRENT_AMOUNT++;
	}
    }

}
