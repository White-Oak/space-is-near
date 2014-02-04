/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components.inventory;

import lombok.Getter;
import spaceisnear.game.objects.items.Size;

/**
 *
 * @author White Oak
 */
public class TypicalInventorySlotsSet {

    @Getter private final InventorySlot head = new InventorySlot(Size.MEDIUM, "head"),
	    body = new InventorySlot(Size.MEDIUM, "body"),
	    leftHand = new InventorySlot(Size.MEDIUM, "left hand"),
	    rightHand = new InventorySlot(Size.MEDIUM, "right hand"),
	    legs = new InventorySlot(Size.MEDIUM, "legs"),
	    shoes = new InventorySlot(Size.MEDIUM, "shoes"),
	    ear = new InventorySlot(Size.SMALL, "ear"),
	    bag = new InventorySlot(Size.MEDIUM, "bag"),
	    belt = new InventorySlot(Size.MEDIUM, "belt"),
	    id = new InventorySlot(Size.SMALL, "id");

    private int[] getItemsIdsForTransferring() {
	return new int[]{head.getItemId(), body.getItemId(), leftHand.getItemId(), rightHand.getItemId(), legs.getItemId(), shoes.getItemId(),
	    ear.getItemId(), bag.getItemId(), belt.getItemId(), id.getItemId()};
    }
    private void setItemsIdsFromTransferring(int id[]){
	
    }
}
