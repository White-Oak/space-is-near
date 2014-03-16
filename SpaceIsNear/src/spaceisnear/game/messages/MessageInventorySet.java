/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import lombok.Getter;
import spaceisnear.abstracts.AbstractGameObject;
import spaceisnear.game.GameContext;
import spaceisnear.game.components.inventory.InventorySlot;
import spaceisnear.game.components.inventory.TypicalInventorySlotsSet;
import spaceisnear.game.messages.properties.MessagePropertable;
import spaceisnear.game.objects.GameObjectType;
import spaceisnear.game.objects.Player;

public class MessageInventorySet extends DirectedMessage implements MessagePropertable {

    @Getter private final HashMap<String, InventorySlot> slots;

    private final static Type typeOfT = new TypeToken<HashMap<String, InventorySlot>>() {
    }.getType();

    public MessageInventorySet(int id, TypicalInventorySlotsSet set) {
	super(MessageType.INVENTORY_SET, id);
	slots = set.getSlots();
    }

    private MessageInventorySet(HashMap<String, InventorySlot> map, int id) {
	super(MessageType.INVENTORY_SET, id);
	slots = map;
    }

    @Override
    public void processForClient(GameContext context) {
	AbstractGameObject get = context.getObjects().get(getId());
	if (get.getType() == GameObjectType.PLAYER || get.getType() == GameObjectType.GAMER_PLAYER) {
	    Player player = (Player) get;
	    player.getInventoryComponent().getSlots().setSlots(getSlots());
	}
    }

}
