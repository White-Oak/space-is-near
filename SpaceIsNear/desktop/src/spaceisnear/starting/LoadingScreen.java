package spaceisnear.starting;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import me.whiteoak.minlog.Log;
import spaceisnear.game.Networking;
import spaceisnear.starting.ui.ScreenImprovedGreatly;

/**
 * @author White Oak
 */
public class LoadingScreen extends ScreenImprovedGreatly {

    public static int LOADING_AMOUNT, CURRENT_AMOUNT;
    private final Label progress;
    private Networking networking;

    public LoadingScreen() {
	Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
	Label loadingLabel = new Label("Loading", labelStyle);
	progress = new Label("0/0", labelStyle);

	int x = (Gdx.graphics.getWidth() - 400) >> 1;
	int y = Gdx.graphics.getHeight() >> 1;

	loadingLabel.setPosition(x - loadingLabel.getWidth() - 20, y);
	progress.setPosition(x, y);

	stage.addActor(loadingLabel);
	stage.addActor(progress);
	setBackgroundColor(new Color(0xecf0f1ff));
    }

    @Override
    public void update() {
	if (networking.isPlayable()) {
	    Log.info("client", "Moving to Core...");
	}
    }

    @Override
    public void draw() {
	progress.setText(CURRENT_AMOUNT + " / " + LOADING_AMOUNT);
	update();
    }

}
