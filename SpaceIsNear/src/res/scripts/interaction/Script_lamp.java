package res.scripts.interaction;

import spaceisnear.game.components.server.scriptsv2.InteractionScript;

/**
 *
 * @author White Oak
 */
public class Script_lamp extends InteractionScript {

    @Override
    public void script() {
	getItem().ifPresent(item -> {
	    boolean isEnabled = getBooleanPropertyOrFalse("lightEnabled");
	    isEnabled = !isEnabled;
	    setPropertyAndSend("lightEnabled", isEnabled);
	    if (!isEnabled) {
		sendAnimationQueueToRequestor("1", 200);
	    } else {
		sendAnimationQueueToRequestor("0", 200);
	    }
	});
    }

    private void setPropertyAndSend(String lightEnabled, boolean enabled) {
    }

}
