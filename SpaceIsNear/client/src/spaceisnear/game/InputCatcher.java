package spaceisnear.game;

import com.badlogic.gdx.scenes.scene2d.*;

/**
 *
 * @author White Oak
 */
public class InputCatcher extends Actor {

    private final Corev2 core;

    public InputCatcher(Corev2 core) {
	this.core = core;
	init(core);
    }

    private void init(final Corev2 core) {
	addCaptureListener(new InputListener() {

	    @Override
	    public boolean keyDown(InputEvent event, int keycode) {
		core.setKey(keycode);
		return true;
	    }

	    @Override
	    public boolean keyUp(InputEvent event, int keycode) {
		core.setKey(0);
		return true;
	    }

	    @Override
	    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		core.mouseClicked(button, (int) x, (int) y);
		getStage().setKeyboardFocus(InputCatcher.this);
		return true;
	    }

	});
    }

}
