/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components.inventory;

import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;
import spaceisnear.game.objects.items.Size;

/**
 *
 * @author White Oak
 */
public class TypicalInventorySlotsSet {

    private final InventorySlot head = new InventorySlot(Size.MEDIUM, "head"),
	    body = new InventorySlot(Size.MEDIUM, "body"),
	    leftHand = new InventorySlot(Size.MEDIUM, "left hand"),
	    rightHand = new InventorySlot(Size.MEDIUM, "right hand"),
	    legs = new InventorySlot(Size.MEDIUM, "legs"),
	    shoes = new InventorySlot(Size.MEDIUM, "shoes"),
	    ear = new InventorySlot(Size.SMALL, "ear"),
	    bag = new InventorySlot(Size.MEDIUM, "bag"),
	    belt = new InventorySlot(Size.MEDIUM, "belt"),
	    id = new InventorySlot(Size.SMALL, "id");
    @Getter @Setter private HashMap<String, InventorySlot> slots = new HashMap<>();

    public TypicalInventorySlotsSet() {
	slots.put("head", head);
	slots.put("body", body);
	slots.put("left hand", leftHand);
	slots.put("right hand", rightHand);
	slots.put("legs", legs);
	slots.put("shoes", shoes);
	slots.put("ear", ear);
	slots.put("bag", bag);
	slots.put("belt", belt);
	slots.put("id", id);
    }

    public InventorySlot get(String key) {
	return slots.get(key);
    }

}
