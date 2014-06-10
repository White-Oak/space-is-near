package res.scripts.interaction;

import spaceisnear.game.components.server.newscripts.InteractionScript;

/**
 *
 * @author White Oak
 */
public class ScriptLamp extends InteractionScript {

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

    private void sendAnimationQueueToRequestor(String string, int i) {
    }

}
