package spaceisnear.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import spaceisnear.abstracts.Context;
import spaceisnear.game.messages.MessageType;
import spaceisnear.game.messages.NetworkableMessage;
import spaceisnear.game.messages.service.onceused.MessageClientInformation;
import spaceisnear.game.messages.service.onceused.MessagePlayerInformation;
import spaceisnear.game.objects.NetworkingObject;
import spaceisnear.game.ui.ActivationListener;
import spaceisnear.game.ui.console.GameConsole;
import spaceisnear.starting.*;

/**
 *
 * @author White Oak
 */
public class Corev3 extends Game implements ActivationListener {

    private final InputMultiplexer multiplexer = new InputMultiplexer();
    private OrthographicCamera camera;
    @Getter private GameConsole console;
    private GameContext context;
    public static BitmapFont font;
    private LoginScreen loginScreen;
    private LoadingScreen loading;
    private Lobby lobby;
    private Corev2 core;
    private ScreenImprovedGreatly[] screens = new ScreenImprovedGreatly[3];

    @Override
    public void create() {
	this.camera = new OrthographicCamera(1200, 600);
	Gdx.input.setInputProcessor(multiplexer);
	camera.setToOrtho(true);
	camera.update();
	font = new BitmapFont(Gdx.files.classpath("default.fnt"), true);
	loginScreen = new LoginScreen(this);
	lobby = new Lobby(this);
	core = new Corev2(this);
	loading = new LoadingScreen(this);
	context = core.getContext();
	initializeConsole();
	screens = new ScreenImprovedGreatly[]{loginScreen, lobby, loading, core};
	setScreen(0);
	new Thread(new Runnable() {

	    @Override
	    public void run() {
		update();
	    }
	}).start();
    }

    public void setScreen(int number) {
	setScreenImproved(screens[number]);
    }

    public void setScreenImproved(ScreenImprovedGreatly screenImproved) {
	if (getScreen() != null) {
	    if (getScreen() instanceof ScreenImprovedGreatly) {
		removeScreenImproved((ScreenImprovedGreatly) getScreen());
	    } else {
		setScreen(null);
	    }
	}
	setScreen(screenImproved);
	Stage stage = screenImproved.getStage();
	multiplexer.addProcessor(stage);
	stage.addActor(console);
	stage.addActor(console.getTextField());
    }

    private void removeScreenImproved(ScreenImproved screenImproved) {
	if (getScreen() != screenImproved) {
	    Stage stage = screenImproved.getStage();
	    multiplexer.removeProcessor(stage);
	    stage.getActors().removeValue(console, true);
	    stage.getActors().removeValue(console.getTextField(), true);
	}
    }

    private void initializeConsole() {
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
	console.processInputedMessage();
	((ScreenImproved) getScreen()).getStage().setKeyboardFocus(null);
    }

    public void send(NetworkableMessage m) {
	if (m.getMessageType() == MessageType.CLIENT_INFO) {
	    core.getNetworking().setMci((MessageClientInformation) m);
	}
	if (m.getMessageType() == MessageType.PLAYER_INFO) {
	    core.getNetworking().setMpi((MessagePlayerInformation) m);
	}
	core.getNetworking().send(m);
    }

    private void update() {
	while (true) {
	    if (!core.isNotpaused()) {
		NetworkingObject get = (NetworkingObject) core.getContext().getObjects().get(Context.NETWORKING_ID);
		get.process();
	    }
	    try {
		Thread.sleep(50);
	    } catch (InterruptedException ex) {
		Logger.getLogger(Corev3.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
    }

    public boolean isCoreNotPaused() {
	return core.isNotpaused();
    }
}
