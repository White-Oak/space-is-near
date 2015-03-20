package spaceisnear.game.components.inventory;

import java.io.Serializable;
import lombok.*;
import spaceisnear.game.objects.items.Size;

/**
 * @author White Oak
 */
@Data @AllArgsConstructor @RequiredArgsConstructor @NoArgsConstructor(access = AccessLevel.PRIVATE) public class InventorySlot implements Serializable {

    private int itemId = -1;
    @NonNull private Size size;
    @NonNull private String name;

    private InventorySlot(InventorySlot slot, String newName) {
	itemId = slot.itemId;
	size = slot.size;
	name = newName;
    }

    public InventorySlot getWithNewName(String newName) {
	return new InventorySlot(this, newName);
    }
}
