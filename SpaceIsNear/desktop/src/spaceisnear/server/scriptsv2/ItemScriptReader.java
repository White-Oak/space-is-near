package spaceisnear.server.scriptsv2;

import spaceisnear.Utils;

/**
 *
 * @author White Oak
 */
public class ItemScriptReader {

    public static ItemScriptBundle[] read() throws Exception {
	return Utils.getJson("/res/scripts/scripts.json", ItemScriptBundle[].class);
    }
}
