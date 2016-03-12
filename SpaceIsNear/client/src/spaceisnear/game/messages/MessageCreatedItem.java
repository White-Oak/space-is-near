/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import lombok.*;
import me.whiteoak.minlog.Log;
import spaceisnear.game.GameContext;
import spaceisnear.game.objects.GameObjectType;
import spaceisnear.game.objects.items.ItemsArchive;
import spaceisnear.game.objects.items.StaticItem;
import spaceisnear.starting.LoadingScreen;

@NoArgsConstructor(access = AccessLevel.PRIVATE) public class MessageCreatedItem extends MessageCreated {

    @Getter private int itemTypeId;

    public MessageCreatedItem(int itemTypeId, int id) {
	super(GameObjectType.ITEM, MessageType.CREATED_SIMPLIFIED_ITEM, id);
	this.itemTypeId = itemTypeId;
    }

    @Override
    public void processForClient(GameContext context) {
	if (!context.exists(getId())) {
	    StaticItem item = ItemsArchive.itemsArchive.getNewItem(getItemTypeId());
	    if (item != null) {
		context.addObject(item, getId());
		LoadingScreen.CURRENT_AMOUNT++;
	    }
	} else {
	    Log.debug("client", getId() + " already exists");
	}
    }

}
