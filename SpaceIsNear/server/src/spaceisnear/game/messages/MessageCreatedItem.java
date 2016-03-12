/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import lombok.*;
import spaceisnear.game.objects.GameObjectType;

@NoArgsConstructor(access = AccessLevel.PRIVATE) public class MessageCreatedItem extends MessageCreated {

    @Getter private int itemTypeId;

    public MessageCreatedItem(int itemTypeId, int id) {
	super(GameObjectType.ITEM, MessageType.CREATED_SIMPLIFIED_ITEM, id);
	this.itemTypeId = itemTypeId;
    }
}
