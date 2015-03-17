package spaceisnear.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.esotericsoftware.minlog.Logs;
import lombok.Getter;
import spaceisnear.abstracts.Context;
import spaceisnear.game.messages.MessageType;
import spaceisnear.game.messages.NetworkableMessage;
import spaceisnear.game.messages.service.onceused.MessageClientInformation;
import spaceisnear.game.messages.service.onceused.MessagePlayerInformation;
import spaceisnear.game.objects.NetworkingObject;
import spaceisnear.game.ui.ActivationListener;
import spaceisnear.game.ui.UIElement;
import spaceisnear.game.ui.console.GameConsole;
import spaceisnear.starting.*;
import spaceisnear.starting.ui.ScreenImproved;
import spaceisnear.starting.ui.ScreenImprovedGreatly;

/**
 *
 * @author White Oak
 */
public class Corev3 extends com.badlogic.gdx.Game implements ActivationListener {

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
	font = new BitmapFont(Gdx.files.classpath("segoe_ui.fnt"), true);
	Logs.info("client", "Font has created -- don't worry");
	loginScreen = new LoginScreen(this);
	lobby = new Lobby(this);
	core = new Corev2(this);
	loading = new LoadingScreen(this);
	context = core.getContext();
	initializeConsole();
	screens = new ScreenImprovedGreatly[]{loginScreen, lobby, loading, core};
	setScreen(0);
	new Thread(this::update, "Corev3").start();
	Thread thread = new Thread(() -> {
	    while (true) {
		System.gc();
		try {
		    Thread.sleep(10000L);
		} catch (InterruptedException ex) {
		    Logs.error("client", "While trying to sleep in GC thread", ex);
		}
	    }
	}, "GC Runner");
	thread.setPriority(Thread.MIN_PRIORITY);
	thread.start();
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
    public void componentActivated(UIElement actor) {
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
	while (getScreen() != screens[3]) {
	    if (!core.isNotpaused()) {
		NetworkingObject get = (NetworkingObject) core.getContext().getObjects().get(Context.NETWORKING_ID);
		get.process();
	    }
	    if (getScreen() == screens[0]) {
		if (isLogined()) {
		    setScreen(1);
		}
	    }
	    if (isJoined()) {
		setScreen(2);
	    }
	    try {
		Thread.sleep(100);
	    } catch (InterruptedException ex) {
		Logs.error("client", "while trying to sleep in Corev3 update thread", ex);
	    }
	}
    }

    public boolean isCoreNotPaused() {
	return core.isNotpaused();
    }

    public boolean isLogined() {
	return core.getNetworking().isLogined();
    }

    public boolean isConnected() {
	return core.getNetworking().isConnected();
    }

    public boolean isJoined() {
	return core.getNetworking().isJoined();
    }
}
