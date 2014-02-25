package spaceisnear.starting;

import spaceisnear.starting.ui.ScreenImprovedGreatly;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import spaceisnear.game.Corev3;

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
	setBackgroundColor(Color.WHITE);
    }

    public void update() {
	if (isCoreNotPaused()) {
	    System.out.println("Moving to Core...");
	    setScreen(3);
	}
    }

    @Override
    public void draw() {
	progress.setText(CURRENT_AMOUNT + " / " + LOADING_AMOUNT);
	update();
    }

}
