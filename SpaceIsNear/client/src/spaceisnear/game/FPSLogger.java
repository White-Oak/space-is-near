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
	final long currentTimeMillis = System.currentTimeMillis();
	if (currentTimeMillis - startTime > 1000) {
	    startTime = currentTimeMillis;
	    if (timesSkipped > 6) {
		Log.debug("client", "fps: " + Gdx.graphics.getFramesPerSecond());
		timesSkipped = 0;
	    } else {
		timesSkipped++;
	    }
	}
    }
}
