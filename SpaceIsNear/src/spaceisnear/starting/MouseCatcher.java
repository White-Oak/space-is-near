package spaceisnear.starting;

import com.badlogic.gdx.scenes.scene2d.*;

/**
 *
 * @author White Oak
 */
public class MouseCatcher extends Actor {

    public MouseCatcher() {
	addListener(new InputListener() {

	    @Override
	    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		getStage().setKeyboardFocus(null);
		return false;
	    }
	});
    }

}
