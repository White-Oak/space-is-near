package res.scripts.interaction;

import spaceisnear.game.components.server.newscripts.InteractionScript;

/**
 *
 * @author White Oak
 */
public class ScriptDoor extends InteractionScript {

    @Override
    public void script() {
	getItem().ifPresent(item -> {
	    boolean isOpened = getBooleanPropertyOrFalse("opened");
	    isOpened = !isOpened;
	    setProperty("opened", isOpened);
	    setFullyPathable(isOpened);
	    if (isOpened == true) {
		sendAnimationQueueToRequestor("0, 1, 2, 3", 200);
		setProperty("ticksSinceOpened", 0);
		registerForTimeMessage(150);
	    } else {
		sendAnimationQueueToRequestor("3, 2, 1, 0", 200);
	    }
	});
    }

    private void setFullyPathable(boolean pathable) {
    }

    private void sendAnimationQueueToRequestor(String queue, int i) {
    }

    private void registerForTimeMessage(int i) {
    }

}
