/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components.inventory;

import spaceisnear.game.objects.items.Size;
import lombok.Getter;
import lombok.Setter;
import spaceisnear.game.objects.items.StaticItem;

/**
 *
 * @author White Oak
 */
public class InventorySlot {

    @Getter @Setter private StaticItem item = null;
    @Getter private Size size;
    @Getter private String name;

    public InventorySlot(Size size, String name) {
	this.size = size;
	this.name = name;
    }

    @Deprecated
    InventorySlot() {
    }
}
