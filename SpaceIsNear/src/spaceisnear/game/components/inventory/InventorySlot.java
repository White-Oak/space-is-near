package spaceisnear.game.components.inventory;

import java.io.Serializable;
import lombok.Data;
import spaceisnear.game.objects.items.Size;

/**
 * @author White Oak
 */
@Data public class InventorySlot implements Serializable {

    private int itemId = -1;
    private final Size size;
    private final String name;
}
