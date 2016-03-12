/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components.inventory;

import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;
import spaceisnear.abstracts.Context;
import spaceisnear.game.messages.Message;
import spaceisnear.game.objects.items.Size;

/**
 *
 * @author White Oak
 */
public final class TypicalInventorySlotsSet {

    private final InventorySlot head = new InventorySlot(Size.MEDIUM, "head"),
	    body = new InventorySlot(Size.MEDIUM, "body"),
	    leftHand = new InventorySlot(Size.MEDIUM, "left hand"),
	    rightHand = new InventorySlot(Size.MEDIUM, "right hand"),
	    shoes = new InventorySlot(Size.MEDIUM, "shoes"),
	    ear = new InventorySlot(Size.SMALL, "ear"),
	    bag = new InventorySlot(Size.MEDIUM, "bag"),
	    belt = new InventorySlot(Size.MEDIUM, "belt"),
	    id = new InventorySlot(Size.SMALL, "id"),
	    mask = new InventorySlot(Size.SMALL, "mask"),
	    costume = new InventorySlot(Size.MEDIUM, "costume"),
	    costumeSlot = new InventorySlot(Size.BIG, "costume slot"),
	    gloves = new InventorySlot(Size.SMALL, "gloves"),
	    leftPocket = new InventorySlot(Size.SMALL, "left pocket"),
	    rightPocket = new InventorySlot(Size.SMALL, "right pocket");
    @Getter @Setter private HashMap<String, InventorySlot> slots = new HashMap<>();

    public TypicalInventorySlotsSet() {
	slots.put("head", head);
	slots.put("body", body);
	slots.put("left hand", leftHand);
	slots.put("right hand", rightHand);
	slots.put("shoes", shoes);
	slots.put("ear", ear);
	slots.put("bag", bag);
	slots.put("belt", belt);
	slots.put("id", id);
	add(mask);
	add(costume);
	add(costumeSlot);
	add(gloves);
	add(leftPocket);
	add(rightPocket);
    }

    public void add(InventorySlot slot) {
	slots.put(slot.getName(), slot);
    }

    public InventorySlot get(String key) {
	return slots.get(key);
    }

    public InventorySlot pull(String key) {
	InventorySlot remove = slots.remove(key);
	if (remove != null) {
	    add(new InventorySlot(remove.getSize(), key));
	}
	return remove;
    }

    public void processMessage(Message message, Context context) {
	slots.values().stream()
		.map(inventorySlot -> inventorySlot.getItemId())
		.filter(itemId -> (itemId != -1))
		.map(itemId -> context.getObjects().get(itemId))
		.forEach(get -> get.message(message));
    }

}
