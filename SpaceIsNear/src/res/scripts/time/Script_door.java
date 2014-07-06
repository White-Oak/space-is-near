package res.scripts.time;

import spaceisnear.server.scriptsv2.TimeScript;

/**
 *
 * @author White Oak
 */
public class Script_door extends TimeScript {

    @Override
    public void script() {
	boolean isOpened = getBooleanPropertyOrFalse("opened");
	if (isOpened) {
	    setFullyPathable(false);
	    setProperty("opened", false);
	    sendAnimationQueueToRequestor(new int[]{3, 2, 1, 0}, 200);
	}
    }

}
