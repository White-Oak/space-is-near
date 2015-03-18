package spaceisnear.starting;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.esotericsoftware.minlog.Logs;
import spaceisnear.game.Corev3;
import spaceisnear.starting.ui.ScreenImprovedGreatly;

/**
 * @author White Oak
 */
public class LoadingScreen extends ScreenImprovedGreatly {

    public static int LOADING_AMOUNT, CURRENT_AMOUNT;
    private final Label progress;

    public LoadingScreen(Corev3 corev3) {
	super(corev3);
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

    public void update() {
	if (isCoreNotPaused()) {
	    Logs.info("client", "Moving to Core...");
	    setScreen(3);
	}
    }

    @Override
    public void draw() {
	progress.setText(CURRENT_AMOUNT + " / " + LOADING_AMOUNT);
	update();
    }

}
