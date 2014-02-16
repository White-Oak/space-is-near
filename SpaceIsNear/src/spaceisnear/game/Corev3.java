package spaceisnear.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import spaceisnear.game.ui.ActivationListener;
import spaceisnear.game.ui.console.GameConsole;
import spaceisnear.starting.LoginScreen;
import spaceisnear.starting.ScreenImproved;

/**
 *
 * @author White Oak
 */
public class Corev3 extends Game implements ActivationListener {

    private final InputMultiplexer multiplexer = new InputMultiplexer();
    private OrthographicCamera camera;
    private GameConsole console;
    private GameContext context;
    public static BitmapFont font;
    private LoginScreen loginScreen;

    @Override
    public void create() {
	this.camera = new OrthographicCamera(1200, 600);
	Gdx.input.setInputProcessor(multiplexer);
	camera.setToOrtho(true);
	camera.update();
	initializeConsole();
	loginScreen = new LoginScreen();
	setScreenImproved(loginScreen);
    }

    private void setScreenImproved(ScreenImproved screenImproved) {
	setScreen(screenImproved);
	Stage stage = screenImproved.getStage();
	multiplexer.addProcessor(stage);
	stage.addActor(console);
	stage.addActor(console.getTextField());
    }

    private void removeScreenImproved(ScreenImproved screenImproved) {
	Stage stage = screenImproved.getStage();
	multiplexer.removeProcessor(stage);
	stage.getActors().removeValue(console, true);
	stage.getActors().removeValue(console.getTextField(), true);
    }

    private void initializeConsole() {
	font = new BitmapFont(Gdx.files.classpath("default.fnt"), true);
	final spaceisnear.game.ui.TextField textField = new spaceisnear.game.ui.TextField();
	textField.setActivationListener(this);
	textField.setBounds(800, Gdx.graphics.getHeight() - textField.getPrefHeight(), 400, textField.getPrefHeight());
	//
	console = new GameConsole(800, 0, 400, 600, context, textField);
	console.setPosition(800, 0);
    }

    @Override
    public void render() {
	super.render();
    }

    @Override
    public void componentActivated(Actor actor) {
//	console.processInputedMessage();
	((ScreenImproved) getScreen()).getStage().setKeyboardFocus(null);
    }
}
