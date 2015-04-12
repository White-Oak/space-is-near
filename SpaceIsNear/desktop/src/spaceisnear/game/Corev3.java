package spaceisnear.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.esotericsoftware.minlog.Logs;
import lombok.Getter;
import spaceisnear.abstracts.Context;
import spaceisnear.game.messages.MessageType;
import spaceisnear.game.messages.NetworkableMessage;
import spaceisnear.game.messages.service.onceused.MessageLogin;
import spaceisnear.game.messages.service.onceused.MessagePlayerInformation;
import spaceisnear.game.objects.NetworkingObject;
import spaceisnear.game.ui.ActivationListener;
import spaceisnear.game.ui.UIElement;
import spaceisnear.game.ui.console.ConsoleListenerImpl;
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
	Gdx.input.setInputProcessor(multiplexer);
	font = new BitmapFont(Gdx.files.internal("segoe_ui.fnt"), true);
	loginScreen = new LoginScreen(this);
	lobby = new Lobby(this);
	core = new Corev2(this);
	loading = new LoadingScreen(this);
	context = core.getContext();
	initializeConsole();
	screens = new ScreenImprovedGreatly[]{loginScreen, lobby, loading, core};
	setScreen(0);
	new Thread(this::update, "Corev3").start();
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
	if (false) {
	    multiplexer.addProcessor(new InputProcessor() {

		@Override
		public boolean keyDown(int keycode) {
		    return true;
		}

		@Override
		public boolean keyUp(int keycode) {
		    return true;

		}

		@Override
		public boolean keyTyped(char character) {
		    return true;

		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		    System.out.println("OH GOD PLEASE ANSWER");
		    return true;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		    return true;

		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
		    return true;

		}

		@Override
		public boolean mouseMoved(int screenX, int screenY) {
		    return true;

		}

		@Override
		public boolean scrolled(int amount) {
		    return true;

		}
	    });
	}
	stage.addActor(console);
	stage.addActor(console.getTextField());
    }

    private void removeScreenImproved(ScreenImproved screenImproved) {
	if (getScreen() != screenImproved) {
	    Stage stage = screenImproved.getStage();
	    multiplexer.removeProcessor(stage);
	    stage.getActors().removeValue(console, true);
	    stage.getActors().removeValue(console.getTextField(), true);
	    stage.dispose();
	}
    }

    private void initializeConsole() {
	final spaceisnear.game.ui.TextField textField = new spaceisnear.game.ui.TextField();
	textField.setActivationListener(this);
	textField.setBounds(800, Gdx.graphics.getHeight() - textField.getPrefHeight(), 400, textField.getPrefHeight());
	//
	console = new GameConsole(800, 0, 400, 600, textField);
	console.setConsoleListener(new ConsoleListenerImpl(context));
	console.setPosition(800, 0);
    }

    @Override
    public void render() {
	super.render();
    }

    @Override
    public void componentActivated(UIElement actor) {
	console.processInputMessage();
	((ScreenImproved) getScreen()).getStage().setKeyboardFocus(null);
    }

    public void send(NetworkableMessage m) {
	if (m.getMessageType() == MessageType.LOGIN_INFO) {
	    core.getNetworking().setMci((MessageLogin) m);
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
		Thread.sleep(100L);
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
