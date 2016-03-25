package spaceisnear.game;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;
import lombok.Getter;
import static spaceisnear.Main.IP;
import spaceisnear.abstracts.AbstractGameObject;
import spaceisnear.game.messages.MessageControlledByInput;
import spaceisnear.game.messages.MessageToSend;
import spaceisnear.game.ui.console.ChatString;
import spaceisnear.starting.LoginScreen;
import spaceisnear.starting.Starter;
import spaceisnear.starting.ui.Corev3;
import spaceisnear.starting.ui.Updatable;

/**
 *
 * @author White Oak
 */
public class Engine implements Updatable {

    @Getter private final Map<Integer, AbstractGameObject> objects;
    private final Queue<MessageControlledByInput> queue = new ConcurrentLinkedQueue<>();
    @Getter private final Networking networking;
    @Getter private final GameContext context;
    @Getter private final Corev2 core;
    private final Corev3 corev3;
    private final Controller controller;
    private boolean started, paused;
    private boolean firstTime = true;

    public Engine(Corev3 corev3) {
	this.objects = new HashMap<>();
	this.corev3 = corev3;
	this.core = new Corev2(this);
	this.networking = new Networking(this);
	this.context = new GameContext(this);
	this.controller = new Controller(this);
    }

    @Override
    public void update() {
	if (firstTime) {
	    final Starter starter = new Starter(corev3.getConsole(), networking);
	    final Future<Networking> callToConnect = starter.callToConnect(IP);
	    LoginScreen loginScreen = (LoginScreen) corev3.getScreen();
	    loginScreen.setNetworking(callToConnect);
	    corev3.getConsole().setConsoleListener(new ConsoleListenerImpl(this));
	    firstTime = false;
	} else {
	    if (corev3.getScreen() != core) {
		if (networking.isJoined()) {
		    corev3.setNextScreen(core);
		}
	    } else if (!paused) {
		queue.forEach(mc -> context.sendDirectedMessage(new MessageToSend(mc)));
		objects.values().forEach(value -> value.process());
		MessageControlledByInput checkMovement = controller.checkMovement();
		if (checkMovement != null) {
		    context.sendToNetwork(checkMovement);
		}
	    }
	    networking.processQueue();
	}
    }

    public void pause() {
	paused = true;
    }

    public void unpause() {
	paused = false;
    }

    public void chat(ChatString log) {
	corev3.getConsole().pushMessage(log);
    }

    public void addControllerToCore() {
	controller.setBounds(0, 0, 800, 600);
	core.getStage().addActor(controller);
	core.getStage().setKeyboardFocus(controller);
    }
}
