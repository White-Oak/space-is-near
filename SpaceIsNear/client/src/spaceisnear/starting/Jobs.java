package spaceisnear.starting;

import java.util.Random;
import spaceisnear.Utils;

/**
 *
 * @author White Oak
 */
public class Jobs {

    public static String getRandomProfession() {
	String[] jobs = Utils.getJson("/res/professions.json", String[].class);
	return jobs[new Random().nextInt(jobs.length)];
    }

}
