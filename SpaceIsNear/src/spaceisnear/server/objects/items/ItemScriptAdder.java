package spaceisnear.server.objects.items;

import java.io.InputStream;
import spaceisnear.Utils;
import spaceisnear.game.objects.items.ItemsReader;

/**
 *
 * @author White Oak
 */
public class ItemScriptAdder {

    public static ItemScriptBundle[] read() throws Exception {
	try (InputStream items = ItemsReader.class.getResourceAsStream("/spaceisnear/server/objects/items/scrupts.json")) {
	    byte[] contents = Utils.getContents(items);
	    return Utils.GSON.fromJson(new String(contents), ItemScriptBundle[].class);
	} catch (Exception e) {
	    throw e;
	}
    }
}
