/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import lombok.Getter;
import spaceisnear.Utils;
import spaceisnear.game.components.inventory.TypicalInventorySlotsSet;
import spaceisnear.game.messages.properties.MessagePropertable;

public class MessageInventorySet extends DirectedMessage implements MessagePropertable {

    @Getter private final TypicalInventorySlotsSet set;

    public MessageInventorySet(int id, TypicalInventorySlotsSet set) {
	super(MessageType.INVENTORY_SET, id);
	this.set = set;
    }

    MessageInventorySet() {
	super(null, 0);
	this.set = null;
    }

    public static MessageInventorySet getInstance(byte[] b) {
	return Utils.GSON.fromJson(new String(b), MessageInventorySet.class);
    }

}
