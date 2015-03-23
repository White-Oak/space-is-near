package spaceisnear.starting;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.util.Random;
import spaceisnear.Utils;

/**
 *
 * @author White Oak
 */
public class Jobs {

    public static String getRandomProfession() {
	FileHandle internal = Gdx.files.internal("res/professions.json");
	String[] jobs = Utils.GSON.fromJson(internal.reader(), String[].class);
	return jobs[new Random().nextInt(jobs.length)];
    }

}
