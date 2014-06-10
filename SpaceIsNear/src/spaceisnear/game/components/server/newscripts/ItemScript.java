package spaceisnear.game.components.server.newscripts;

import java.util.Optional;
import spaceisnear.server.objects.items.StaticItem;

/**
 *
 * @author White Oak
 */
public abstract class ItemScript extends CustomScript {

    private StaticItem item;

    protected boolean getBooleanPropertyOrFalse(String name) {
	Optional<Object> property = getProperty(name);
	boolean is = false;
	if (property.isPresent()) {
	    assert property.get().getClass() == Boolean.class;
	    is = (Boolean) property.get();
	}
	return is;
    }

    protected Optional<Object> getProperty(String name) {
	return item.getVariableProperties().getProperty(name);
    }

    protected void setProperty(String name, Object value) {
    }

    protected Optional<StaticItem> getItem() {
	return Optional.of(item);
    }
}
