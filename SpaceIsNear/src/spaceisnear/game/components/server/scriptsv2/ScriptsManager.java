package spaceisnear.game.components.server.scriptsv2;

import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import spaceisnear.Utils;
import spaceisnear.game.objects.items.ItemsReader;

/**
 * This is actually v2.
 *
 * @author White Oak
 */
public class ScriptsManager {

    private final HashMap<String, String> accociates = new HashMap<>();
    @Getter private final ItemScriptBundle[] scripts;
    private final HashMap<String, CustomScript> cache[] = new HashMap[5];

    public ScriptsManager() throws Exception {
	this.scripts = ItemScriptReader.read();
	AssociatesReader.fillWithAssociates(accociates);
	for (int i = 0; i < cache.length; i++) {
	    cache[i] = new HashMap<>();
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

    public CustomScript getScriptFor(ScriptType mode, String itemName) {
	String dirByMode = getDirByMode(mode);
	CustomScript result = cache[mode.ordinal()].get(itemName);
	if (result == null) {
	    try {
		Class ref = Class.forName(dirByMode + "Script_" + itemName);
		Object newInstance = ref.newInstance();
		result = (CustomScript) newInstance;
	    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
		Logger.getLogger(ScriptsManager.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
	return result;
    }

    public static enum ScriptType {

	INTERACTION, MESSAGES, TIME, CONREQ, CONPROC
    }

    private static class AssociatesReader {

	static void fillWithAssociates(HashMap<String, String> map) throws Exception {
	    Associate[] read = read();
	    for (int i = 0; i < read.length; i++) {
		Associate associates = read[i];
		map.put(associates.name, associates.scriptName);
	    }
	}

	private static Associate[] read() throws Exception {
	    try (InputStream items = ItemsReader.class.getResourceAsStream("/res/scripts/associates.json")) {
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
