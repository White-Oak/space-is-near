package res.scripts.interaction;

import spaceisnear.game.messages.MessageToSend;
import spaceisnear.game.messages.properties.MessagePropertySet;
import spaceisnear.server.scriptsv2.InteractionScript;

/**
 *
 * @author White Oak
 */
public class Script_lamp extends InteractionScript {

    @Override
    public void script() {
	boolean isEnabled = getBooleanPropertyOrFalse("lightEnabled");
	isEnabled = !isEnabled;
	setPropertyAndSend("lightEnabled", isEnabled);
	if (!isEnabled) {
	    sendAnimationQueueToRequestor(new int[]{1}, 200);
	} else {
	    sendAnimationQueueToRequestor(new int[]{0}, 200);
	}
    }

    private void setPropertyAndSend(String lightEnabled, boolean enabled) {
	setProperty(lightEnabled, enabled);
	MessageToSend messageToSend = new MessageToSend(new MessagePropertySet(getItem().getId(),
		lightEnabled, String.valueOf(enabled)));
	context.sendDirectedMessage(messageToSend);
    }

}
