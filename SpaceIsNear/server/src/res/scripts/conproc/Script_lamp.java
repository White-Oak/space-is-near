package res.scripts.conproc;

import spaceisnear.server.scriptsv2.ContextProcessorScript;

/**
 *
 * @author White Oak
 */
public class Script_lamp extends ContextProcessorScript {

    @Override
    public void script() {
	int chosen = getChosen();
	if (chosen == 3) {
	    interact();
	}
    }

}
