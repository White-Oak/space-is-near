/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages.properties;

import java.util.HashMap;
import lombok.*;
import spaceisnear.game.components.inventory.InventorySlot;
import spaceisnear.game.components.inventory.TypicalInventorySlotsSet;
import spaceisnear.game.messages.DirectedMessage;
import spaceisnear.game.messages.MessageType;

@NoArgsConstructor(access = AccessLevel.PRIVATE) public class MessageInventorySet extends DirectedMessage implements MessagePropertable {

    @Getter private HashMap<String, InventorySlot> slots;

    public MessageInventorySet(int id, TypicalInventorySlotsSet set) {
	super(MessageType.INVENTORY_SET, id);
	slots = set.getSlots();
    }

    private MessageInventorySet(HashMap<String, InventorySlot> map, int id) {
	super(MessageType.INVENTORY_SET, id);
	slots = map;
    }

}
