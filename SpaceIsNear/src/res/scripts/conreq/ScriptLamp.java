package res.scripts.conreq;

import spaceisnear.game.components.server.newscripts.ContextRequestScript;

/**
 *
 * @author White Oak
 */
public class ScriptLamp extends ContextRequestScript {

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
