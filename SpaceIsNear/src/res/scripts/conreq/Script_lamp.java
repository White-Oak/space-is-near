package res.scripts.conreq;

import spaceisnear.game.components.server.scriptsv2.ContextRequestScript;

/**
 *
 * @author White Oak
 */
public class Script_lamp extends ContextRequestScript {

    @Override
    public void script() {
	addDefaultActions();
	boolean isEnabled = getBooleanPropertyOrFalse("lightEnabled");
	if (isEnabled) {
	    add("Turn off");
	} else {
	    add("Turn on");
	}
    }

}
