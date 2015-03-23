package spaceisnear.server.scriptsv2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import spaceisnear.Utils;

/**
 *
 * @author White Oak
 */
public class ItemScriptReader {

    public static ItemScriptBundle[] read() throws Exception {
	FileHandle internal = Gdx.files.internal("res/scripts/scripts.json");
	return Utils.GSON.fromJson(internal.reader(), ItemScriptBundle[].class);
    }
}
