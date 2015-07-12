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
    private boolean started, paused;
    private boolean firstTime = true;

    public Engine(Corev3 corev3) {
	this.objects = Collections.synchronizedMap(new HashMap<>());
	this.corev3 = corev3;
	this.core = new Corev2(this);
	this.context = new GameContext(this);
	this.networking = new Networking(this);
    }

    @Override
    public void update() {
	if (firstTime) {
	    final Starter starter = new Starter(corev3.getConsole(), networking);
	    final Future<Networking> callToConnect = starter.callToConnect(IP);
	    LoginScreen loginScreen = (LoginScreen) corev3.getScreen();
	    loginScreen.setNetworking(callToConnect);
	    firstTime = false;
	} else {
	    networking.processQueue();
	    if (!started) {
		if (networking.isPlayable()) {
		    started = networking.isPlayable();
		    corev3.setScreenImproved(core);
		}
	    } else {
		if (!paused) {
		    queue.forEach(mc -> context.sendDirectedMessage(new MessageToSend(mc)));
		    objects.values()
			    .forEach(gameObject -> gameObject.process());
		}
	    }
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
}
