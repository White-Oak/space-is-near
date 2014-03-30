package spaceisnear.game.components;

import lombok.RequiredArgsConstructor;

/**
 *
 * @author White Oak
 */
@RequiredArgsConstructor
class OneShotTask {

    private final Taskable task;
    private final int ticksToPass;
    private int ticksPassed;

    /**
     *
     * @return true - if task has processed, false - otherwise
     */
    public boolean tick() {
	ticksPassed++;
	if (isReadyToStart()) {
	    process();
	    return true;
	}
	return false;
    }

    private boolean isReadyToStart() {
	return ticksPassed > ticksToPass;
    }

    private void process() {
	task.doIt();
    }
}
