package spaceisnear.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.*;
import spaceisnear.game.messages.MessageControlledByInput;
import spaceisnear.game.messages.MessageToSend;
import spaceisnear.game.messages.properties.MessagePropertySet;

/**
 *
 * @author White Oak
 */
public class Controller extends Actor {

    private boolean wantsToMove;
    /*
     * 1  -- UP
     * 3  -- DOWN
     * 4  -- RIGHT
     * 12 -- LEFT
     * 0 0 0 0 1 1 1 1
     * LEFT ---| | | |
     * RIGHT ----| | |
     * DOWN -------| |
     * UP -----------|
     */
    private int direction;

    private final Engine engine;
    private long lastTimeMoved;
    private final static long MINIMUM_TIME_TO_MOVE = 100L;

    public Controller(Engine engine) {
	this.engine = engine;
	addListener();
    }

    private GameContext getContext() {
	return engine.getContext();
    }

    private void checkControlKeys(int key) {
	switch (key) {
	    case Input.Keys.ESCAPE:
		Integer property = (Integer) getContext().getPlayer().getVariablePropertiesComponent().getProperty("pull");
		if (property != -1) {
		    MessagePropertySet messagePropertySet = new MessagePropertySet(getContext().getPlayerID(), "pull", -1);
		    MessageToSend messageToSend = new MessageToSend(messagePropertySet);
		    getContext().sendDirectedMessage(messageToSend);
		}
		break;
	    case Input.Keys.ENTER:
//		final boolean focused = getConsole().getTextField().isFocused();
//		if (!focused) {
//		    key = 0;
//		    stage.setKeyboardFocus(getConsole().getTextField());
//		}
		break;
	    case Input.Keys.M:
//		inventory.setMinimized(!inventory.isMinimized());
		lastTimeMoved = System.currentTimeMillis();
		break;
	    case Input.Keys.R:
//		inventory.changeActiveHand();
		lastTimeMoved = System.currentTimeMillis();
		break;
	}
    }

    private void checkMovementKeyDown(int key) {
	switch (key) {
	    case Input.Keys.UP:
		direction |= 0b00000001;
		break;
	    case Input.Keys.DOWN:
		direction |= 0b00000011;
		break;
	    case Input.Keys.LEFT:
		direction |= 0b00001100;
		break;
	    case Input.Keys.RIGHT:
		direction |= 0b00000100;
		break;
	}
	wantsToMove = direction != 0;
    }

    private void checkMovementKeyUp(int key) {
	switch (key) {
	    case Input.Keys.UP:
		direction &= ~0b00000001;
		break;
	    case Input.Keys.DOWN:
		direction &= ~0b00000011;
		break;
	    case Input.Keys.LEFT:
		direction &= ~0b00001100;
		break;
	    case Input.Keys.RIGHT:
		direction &= ~0b00000100;
		break;
	}
	wantsToMove = direction != 0;
    }

    public MessageControlledByInput checkMovement() {
	MessageControlledByInput mc = null;
	if (getContext().getPlayer() != null) {
	    boolean ableToMove = System.currentTimeMillis() - lastTimeMoved > MINIMUM_TIME_TO_MOVE
		    && !getContext().getPlayer().getPositionComponent().isAnimated();
//		    && !getContext().getPlayer().getPositionComponent().isAnimated() && !getConsole().hasFocus();
	    if (ableToMove) {
		MessageControlledByInput.Type type = getType(direction);
		if (type != null) {
		    mc = new MessageControlledByInput(type, getContext().getPlayerID());
		    lastTimeMoved = System.currentTimeMillis();
		}
	    }
	}
	return mc;
    }

    private MessageControlledByInput.Type getType(int direction) {
	switch (direction) {
	    case 1:
		return MessageControlledByInput.Type.UP;
	    case 3:
		return MessageControlledByInput.Type.DOWN;
	    case 4:
		return MessageControlledByInput.Type.RIGHT;
	    case 12:
		return MessageControlledByInput.Type.LEFT;
	    case 5:
		return MessageControlledByInput.Type.UPRIGHT;
	    case 13:
		return MessageControlledByInput.Type.UPLEFT;
	    case 7:
		return MessageControlledByInput.Type.DOWNRIGHT;
	    case 15:
		return MessageControlledByInput.Type.DOWNLEFT;
	    default:
		return null;
	}
    }

    private void akeyDown(int key) {
	checkMovementKeyDown(key);
	checkControlKeys(key);
    }

    private void akeyUp(int key) {
	checkMovementKeyUp(key);
    }

    private void addListener() {
	addListener(new InputListener() {

	    @Override
	    public boolean keyDown(InputEvent event, int keycode) {
		System.out.println("Gotcha!");
		akeyDown(keycode);
		return true;
	    }

	    @Override
	    public boolean keyUp(InputEvent event, int keycode) {
		akeyUp(keycode);
		return true;
	    }

	});
    }
}
