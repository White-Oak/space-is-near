package spaceisnear.game;

import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.Getter;
import spaceisnear.abstracts.AbstractGameObject;
import spaceisnear.game.messages.MessageControlledByInput;
import spaceisnear.game.messages.MessageToSend;
import spaceisnear.starting.ui.Corev3;
import spaceisnear.starting.ui.Updatable;

/**
 *
 * @author White Oak
 */
public class Engine implements Updatable {

    @Getter private HashMap<Integer, AbstractGameObject> objects;
    private final Queue<MessageControlledByInput> queue = new ConcurrentLinkedQueue<>();
    @Getter private final Networking networking;
    @Getter private final GameContext context;
    @Getter private final Corev2 core;
    private final Corev3 corev3;
    private boolean started;

    public Engine(Corev3 corev3) {
	this.corev3 = corev3;
	this.core = new Corev2(this);
	this.context = new GameContext(this);
	this.networking = new Networking(this);

    }

    @Override
    public void update() {
	if (!started) {
	    if (networking.isPlayable()) {
		started = networking.isPlayable();
		corev3.setScreenImproved(core);
	    }
	} else {
	    queue.forEach(mc -> context.sendDirectedMessage(new MessageToSend(mc)));
	    objects.values()
		    .forEach(gameObject -> gameObject.process());
	}
    }
}
