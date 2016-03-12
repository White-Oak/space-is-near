package spaceisnear.game.components;

import lombok.RequiredArgsConstructor;

/**
 *
 * @author White Oak
 */
@RequiredArgsConstructor
public class OneShotTask {

    private final Taskable task;
    private final int ticksToPass;
    private int ticksPassed;
    private boolean done;

    /**
     *
     * @return true - if task has processed, false - otherwise
     */
    public synchronized boolean tick() {
	ticksPassed++;
	if (!done && isReadyToStart()) {
	    process();
	}
	return done;
    }

    private boolean isReadyToStart() {
	return ticksPassed > ticksToPass;
    }

    private void process() {
	task.doIt();
	done = true;
    }
}
