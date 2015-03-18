package res.scripts.interaction;

import java.util.List;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentType;
import spaceisnear.game.components.inventory.InventoryComponent;
import spaceisnear.server.objects.ServerGameObject;
import spaceisnear.server.objects.items.StaticItem;
import spaceisnear.server.scriptsv2.InteractionScript;

/**
 *
 * @author White Oak
 */
public class Script_door extends InteractionScript {

    @Override
    public void script() {
	boolean isOpened = getBooleanPropertyOrFalse("opened");
	isOpened = !isOpened;
	//if closing the door than who cares
	//if opening then check for allowance
	if (!isOpened || checkAllowance()) {
	    setProperty("opened", isOpened);
	    setFullyPathable(isOpened);
	    if (isOpened == true) {
		sendAnimationQueueToRequestor(new int[]{0, 1, 2, 3}, 200);
		setProperty("ticksSinceOpened", 0);
		registerForTimeMessage(150);
	    } else {
		sendAnimationQueueToRequestor(new int[]{3, 2, 1, 0}, 200);
	    }
	}
    }

    private boolean checkAllowance() {
	boolean allowed = false;
	String allowedString = (String) getProperty("allowed");
	if (allowedString != null) {
	    ServerGameObject interactor = getInteractor();
	    List<Component> components = interactor.getComponents();
	    for (Component component : components) {
		if (component.getType() == ComponentType.INVENTORY) {
		    InventoryComponent ic = (InventoryComponent) component;
		    int itemId = ic.getSlots().get("id").getItemId();
		    if (itemId != -1) {
			StaticItem card = (StaticItem) context.getObjects().get(itemId);
			String profession = (String) card.getVariableProperties().getProperty("profession");
			assert profession != null;
			allowed = allowedString.contains(profession);
		    }
		}
	    }
	}
	return allowed;
    }

}
