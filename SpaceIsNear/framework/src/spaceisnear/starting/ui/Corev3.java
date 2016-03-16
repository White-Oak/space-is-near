package spaceisnear.starting.ui;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import lombok.Getter;
import lombok.Setter;
import spaceisnear.game.ui.console.GameConsole;
import spaceisnear.game.ui.console.GameConsolev2;

/**
 * @author White Oak
 */
public final class Corev3 extends com.badlogic.gdx.Game {

    private final InputMultiplexer multiplexer = new InputMultiplexer();
    public static BitmapFont font;
    @Getter private OrthographicCamera camera;
    @Getter private Viewport viewport;
    @Setter private ScreenImprovedGreatly startingScreen;
    @Setter private Updatable updatable;
    private final GameConsolev2 consolev2 = new GameConsolev2();
    private Batch savedBatch;
    //
    @Setter private ScreenImprovedGreatly nextScreen;

    @Override
    public ScreenImprovedGreatly getScreen() {
	return (ScreenImprovedGreatly) super.getScreen(); //To change body of generated methods, choose Tools | Templates.
    }

    public GameConsole getConsole() {
	return consolev2.getConsole();
    }

    @Override public void create() {
	Gdx.input.setInputProcessor(multiplexer);
	font = new BitmapFont(Gdx.files.internal("segoe_ui.fnt"), true);
	viewport = new ExtendViewport(800, 600);
	Stage consoleStage = new Stage(viewport);
	savedBatch = consoleStage.getBatch();
	multiplexer.addProcessor(consoleStage);
	camera = (OrthographicCamera) viewport.getCamera();
	camera.setToOrtho(true);
	consolev2.init(consoleStage);
	if (startingScreen != null) {
	    setScreenImproved(startingScreen);
	}
	new Thread(this::update, "Corev3").start();
    }

    public void setScreenImproved(ScreenImprovedGreatly screenImproved) {
	ScreenImprovedGreatly removePrevious = null;
	if (getScreen() != null) {
	    assert getScreen() instanceof ScreenImprovedGreatly;
	    removePrevious = (ScreenImprovedGreatly) getScreen();
	}
	screenImproved.initialize(this, savedBatch);
	screenImproved.create();
	super.setScreen(screenImproved);
	if (removePrevious != null) {
	    removeScreenImproved(removePrevious);
	}
	multiplexer.addProcessor(screenImproved.getStage());
    }

    @Override
    @Deprecated
    public void setScreen(Screen screen) {
	throw new IllegalAccessError("You can't just add a screen! Use setScreenImproved");
    }

    private void removeScreenImproved(ScreenImprovedGreatly screenImproved) {
	Stage stage = screenImproved.getStage();
	multiplexer.removeProcessor(stage);
	screenImproved.dispose();
	stage.dispose();
    }

    @Override public void render() {
	if (nextScreen != null) {
	    setScreenImproved(nextScreen);
	    nextScreen = null;
	}
	super.render();
	consolev2.draw();
    }

    private void update() {
	while (true) {
	    if (getScreen() != null) {
		assert getScreen() instanceof ScreenImprovedGreatly;
		((ScreenImprovedGreatly) getScreen()).update();
//		while (getScreen() != screens[3]) {
//	    if (!core.isNotpaused()) {
//		NetworkingObject get = (NetworkingObject) core.getContext().getObjects().get(Context.NETWORKING_ID);
//		get.process();
//	    }
//	    if (getScreen() == screens[0]) {
//		if (isLogined()) {
//		    setScreen(1);
//		}
//	    }
//	    if (isJoined()) {
//		setScreen(2);
//	    }
//	    try {
//		Thread.sleep(100L);
//	    } catch (InterruptedException ex) {
//		Log.error("client", "while trying to sleep in Corev3 update thread", ex);
//	    }
//	}
	    }
	    if (updatable != null) {
		updatable.update();
	    }
	}
    }

}
