package res.scripts.conreq;

import spaceisnear.game.components.server.newscripts.ContextRequestScript;

/**
 *
 * @author White Oak
 */
public final class ScriptDoor extends ContextRequestScript {

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
