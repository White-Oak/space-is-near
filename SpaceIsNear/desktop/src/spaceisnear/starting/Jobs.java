package spaceisnear.starting;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import spaceisnear.Utils;
import spaceisnear.game.objects.items.ItemsReader;

/**
 *
 * @author White Oak
 */
public class Jobs {

    public static String getRandomProfession() {
	InputStream is = ItemsReader.class.getResourceAsStream("/res/professions.json");
	InputStreamReader isr = new InputStreamReader(is);
	String[] jobs = Utils.GSON.fromJson(isr, String[].class);
	return jobs[new Random().nextInt(jobs.length)];
    }

}
