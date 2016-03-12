package res.scripts.conreq;

import spaceisnear.server.scriptsv2.ContextRequestScript;

/**
 *
 * @author White Oak
 */
public final class Script_door extends ContextRequestScript {

    @Override
    public void script() {
	addDefaultActions();
	boolean isOpened = getBooleanPropertyOrFalse("opened");
	if (isOpened) {
	    add("Close");
	} else {
	    add("Open");
	}
    }

}
