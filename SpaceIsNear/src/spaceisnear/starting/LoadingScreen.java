package spaceisnear.starting;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import spaceisnear.game.Corev2;
import spaceisnear.game.Corev3;

/**
 * @author White Oak
 */
public class LoadingScreen extends ScreenImprovedGreatly {

    private Corev2 core;
    public static int LOADING_AMOUNT, CURRENT_AMOUNT;
    Table table;
    private final Label progress;

    public LoadingScreen(Corev3 corev3) {
	super(corev3);
	table = new Table();
	final Skin skin = new Skin(Gdx.files.classpath("uiskin.json"));
	Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.YELLOW);
	Label loadingLabel = new Label("Loading", labelStyle);
	progress = new Label("0/0", labelStyle);
	table.setFillParent(true);
	table.add(loadingLabel);
	table.row();
	table.add(progress);
	stage.addActor(table);
	setBackgroundColor(Color.BLACK);
    }

    public void update() {
	if (core.isNotpaused()) {
	    System.out.println("Moving to Core...");
//	    Main.main.setScreen(core);
	}
    }

    @Override
    public void draw() {
	progress.setText(CURRENT_AMOUNT + " / " + LOADING_AMOUNT);
	update();
    }

}
