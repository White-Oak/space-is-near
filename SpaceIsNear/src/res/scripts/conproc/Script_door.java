package res.scripts.conproc;

import spaceisnear.game.components.server.scriptsv2.ContextProcessorScript;

/**
 *
 * @author White Oak
 */
public class Script_door extends ContextProcessorScript {

    @Override
    public void script() {
	int chosen = getChosen();
	if (chosen == 0) {
	    interact();
	}
    }

}
