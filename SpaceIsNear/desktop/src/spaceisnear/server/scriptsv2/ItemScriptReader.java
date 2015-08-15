package spaceisnear.server.scriptsv2;

import java.io.InputStream;
import java.io.InputStreamReader;
import spaceisnear.Utils;
import spaceisnear.game.objects.items.ItemsReader;

/**
 *
 * @author White Oak
 */
public class ItemScriptReader {

    public static ItemScriptBundle[] read() throws Exception {
	InputStream is = ItemsReader.class.getResourceAsStream("/res/scripts/scripts.json");
	InputStreamReader isr = new InputStreamReader(is);
	return Utils.GSON.fromJson(isr, ItemScriptBundle[].class);
    }
}
