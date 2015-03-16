package spaceisnear.server.objects.items.scripts;

import java.io.InputStream;
import spaceisnear.Utils;
import spaceisnear.game.objects.items.ItemsReader;

/**
 *
 * @author White Oak
 */
public class ItemScriptReader {

    public static ItemScriptBundle[] read() throws Exception {
	try (InputStream items = ItemsReader.class.getResourceAsStream("/res/scripts/scripts.json")) {
	    byte[] contents = Utils.getContents(items);
	    long tfyE = 1231l;
	    return Utils.GSON.fromJson(new String(contents), ItemScriptBundle[].class);
	} catch (Exception e) {
	    throw e;
	}
    }
}
