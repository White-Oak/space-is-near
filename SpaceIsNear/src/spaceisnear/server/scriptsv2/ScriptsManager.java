package spaceisnear.server.scriptsv2;

import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import spaceisnear.Utils;

/**
 * This is actually v2.
 *
 * @author White Oak
 */
public class ScriptsManager {

    private final HashMap<String, String> associates = new HashMap<>();
    @Getter private ItemScriptBundle[] scripts;
    private final HashMap<String, CustomScript> cache[] = new HashMap[5];

    public ScriptsManager() {
	try {
	    this.scripts = ItemScriptReader.read();
	    AssociatesReader.fillWithAssociates(associates);
	    for (int i = 0; i < cache.length; i++) {
		cache[i] = new HashMap<>();
	    }
	} catch (Exception ex) {
	    Logger.getLogger(ScriptsManager.class.getName()).log(Level.SEVERE, null, ex);
	    throw new RuntimeException();
	}
    }

    private String getDirByMode(ScriptType mode) {
	String dir = null;
	switch (mode) {
	    case INTERACTION:
		dir = "interaction.";
		break;
	    case MESSAGES:
		dir = "messages.";
		break;
	    case TIME:
		dir = "time.";
		break;
	    case CONREQ:
		dir = "conreq.";
		break;
	    case CONPROC:
		dir = "conproc.";
		break;
	}
	return "res.scripts." + dir;
    }

    /**
     * Gets script for ASSOCIATED name (lamp_danger -> lamp).
     *
     * @param mode
     * @param itemName
     * @return
     */
    public CustomScript getScriptFor(ScriptType mode, String itemName) {
	String ass = associates.get(itemName);
	if (ass != null) {
	    String dirByMode = getDirByMode(mode);
	    CustomScript result = cache[mode.ordinal()].get(ass);
	    if (result == null) {
		boolean resultB = false;
		//TODO: fix with HashMap
		for (ItemScriptBundle itemScriptBundle : scripts) {
		    if (itemScriptBundle.scriptName.equals(ass)) {
			resultB = checkIfHasScriptFor(itemScriptBundle, mode);
			break;
		    }
		}
		if (resultB) {
		    try {
			Class ref = Class.forName(dirByMode + "Script_" + ass);
			Object newInstance = ref.newInstance();
			result = (CustomScript) newInstance;
		    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
			Logger.getLogger(ScriptsManager.class.getName()).log(Level.SEVERE, null, ex);
		    }
		}
	    }
	    return result;
	}
	return null;
    }

    private boolean checkIfHasScriptFor(ItemScriptBundle bundle, ScriptType type) {
	switch (type) {
	    case INTERACTION:
		return bundle.interaction;
	    case CONPROC:
		return bundle.contextMenuProccessing;
	    case CONREQ:
		return bundle.contextMenuRequesting;
	    case MESSAGES:
		return bundle.messageProcessing;
	    case TIME:
		return bundle.time;
	    default:
		return false;
	}
    }

    public static enum ScriptType {

	INTERACTION, MESSAGES, TIME, CONREQ, CONPROC
    }

    private static class AssociatesReader {

	static void fillWithAssociates(HashMap<String, String> map) throws Exception {
	    Associate[] read = read();
	    for (Associate associates : read) {
		map.put(associates.name, associates.scriptName);
	    }
	}

	private static Associate[] read() throws Exception {
	    try (InputStream items = AssociatesReader.class.getResourceAsStream("/res/scripts/associates.json")) {
		byte[] contents = Utils.getContents(items);
		return Utils.GSON.fromJson(new String(contents), Associate[].class);
	    } catch (Exception e) {
		throw e;
	    }
	}

	class Associate {

	    String name;
	    String scriptName;
	}
    }
}
