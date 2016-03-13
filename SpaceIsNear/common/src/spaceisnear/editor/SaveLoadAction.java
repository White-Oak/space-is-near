package spaceisnear.editor;

import java.util.List;
import lombok.Value;

/**
 *
 * @author White Oak
 */
@Value public class SaveLoadAction {

    private int itemId;
    private int x, y;
    private ItemProperty[] properties;

    public SaveLoadAction(Item item) {
	this.itemId = item.getId();
	this.x = item.getX();
	this.y = item.getY();
	final List<ItemProperty> properties = item.getProperties();
	this.properties = properties.isEmpty() ? null : properties.toArray(new ItemProperty[0]);
    }

}
