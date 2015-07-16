package spaceisnear.game;

import com.badlogic.gdx.Gdx;
import me.whiteoak.minlog.Log;

/**
 * A simple helper class to log the frames per seconds achieved.
 *
 * @author White Oak
 */
public class FPSLogger {

    private long startTime;
    private int timesSkipped;

    public FPSLogger() {
	startTime = System.currentTimeMillis();
    }

    /**
     * Logs the current frames per second to the console.
     */
    public void log() {
	if (System.currentTimeMillis() - startTime > 1000) {
	    if (timesSkipped > 7) {
		Log.debug("client", "fps: " + Gdx.graphics.getFramesPerSecond());
		startTime = System.currentTimeMillis();
		timesSkipped = 0;
	    } else {
		timesSkipped++;
	    }
	}
    }
}
