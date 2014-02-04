package spaceisnear.game.components.inventory;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import spaceisnear.game.objects.items.Size;
import spaceisnear.game.objects.items.StaticItem;

/**
 * @author White Oak
 */
@Data public class InventorySlot {

    private int itemId = -1;
    private final Size size;
    private final String name;
}
