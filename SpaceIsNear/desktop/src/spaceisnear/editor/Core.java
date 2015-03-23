package spaceisnear.editor;

import com.badlogic.gdx.Game;

/**
 *
 * @author White Oak
 */
public class Core extends Game {

    @Override
    public void create() {
	setScreen(new Editor());
    }
}
