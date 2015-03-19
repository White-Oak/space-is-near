package spaceisnear.starting;

import com.esotericsoftware.minlog.Logs;
import java.io.InputStream;
import java.util.Random;
import spaceisnear.Utils;
import spaceisnear.game.objects.items.ItemsReader;

/**
 *
 * @author White Oak
 */
public class Jobs {

    public static String getRandomProfession() {
	try (InputStream items = ItemsReader.class.getResourceAsStream("/res/professions.json")) {
	    byte[] contents = Utils.getContents(items);
	    String[] jobs = Utils.GSON.fromJson(new String(contents), String[].class);
	    return jobs[new Random().nextInt(jobs.length)];
	} catch (Exception e) {
	    Logs.error("client", "While trying to read professions", e);
	    System.exit(1);
	}
	return null;
    }

}
