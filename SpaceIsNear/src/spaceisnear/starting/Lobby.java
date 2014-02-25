package spaceisnear.starting;

import com.badlogic.gdx.graphics.Color;
import spaceisnear.game.Corev3;

/**
 *
 * @author White Oak
 */
public class Lobby extends ScreenImprovedGreatly {

    public Lobby(Corev3 corev3) {
	super(corev3);
	init();
    }

    private void init() {
	addMouseCatcher();

	setBackgroundColor(Color.WHITE);
    }

}
