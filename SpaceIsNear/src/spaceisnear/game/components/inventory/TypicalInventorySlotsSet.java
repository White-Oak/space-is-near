/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components.inventory;

import spaceisnear.game.objects.items.Size;

/**
 *
 * @author White Oak
 */
public class TypicalInventorySlotsSet {

    private InventorySlot head = new InventorySlot(Size.MEDIUM, "head"),
	    body = new InventorySlot(Size.MEDIUM, "body"),
	    leftHand = new InventorySlot(Size.MEDIUM, "left hand"),
	    rightHand = new InventorySlot(Size.MEDIUM, "right hand"),
	    legs = new InventorySlot(Size.MEDIUM, "legs"),
	    shoes,
	    ear,
	    bag,
	    belt,
	    id;
}
