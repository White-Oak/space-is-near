package spaceisnear.game.components.server.scriptsv2;

import java.util.Optional;
import spaceisnear.server.objects.items.StaticItem;

/**
 *
 * @author White Oak
 */
public abstract class ItemScript extends CustomScript {

    private StaticItem item;

    protected boolean getBooleanPropertyOrFalse(String name) {
	Object property = getProperty(name);
	boolean is = false;
	if (property != null) {
	    assert property.getClass() == Boolean.class;
	    is = (Boolean) property;
	}
	return is;
    }

    protected Object getProperty(String name) {
	return item.getVariableProperties().getProperty(name);
    }

    protected void setProperty(String name, Object value) {
    }

    protected Optional<StaticItem> getItem() {
	return Optional.of(item);
    }

    protected void setFullyPathable(boolean pathable) {
    }

    protected void sendAnimationQueueToRequestor(String queue, int i) {
    }

    protected void registerForTimeMessage(int i) {
    }

}
