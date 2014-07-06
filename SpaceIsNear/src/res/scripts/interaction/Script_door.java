package res.scripts.interaction;

import spaceisnear.server.scriptsv2.InteractionScript;

/**
 *
 * @author White Oak
 */
public class Script_door extends InteractionScript {

    @Override
    public void script() {
	System.out.println("hey");
	boolean isOpened = getBooleanPropertyOrFalse("opened");
	isOpened = !isOpened;
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
