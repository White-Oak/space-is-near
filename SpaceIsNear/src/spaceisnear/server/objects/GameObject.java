/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.server.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import spaceisnear.game.bundles.ObjectBundle;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentState;
import spaceisnear.game.components.ComponentStateBundle;
import spaceisnear.game.messages.Message;
import spaceisnear.game.objects.GameObjectState;
import spaceisnear.game.objects.GameObjectType;
import spaceisnear.server.GameContext;

/**
 *
 * @author White Oak
 */
public abstract class GameObject {

    private final ConcurrentLinkedQueue<Message> messages = new ConcurrentLinkedQueue<>();
    @Getter private int id = -1;
    @Getter @Setter(AccessLevel.PROTECTED) private boolean destroyed = false;
    @Getter(AccessLevel.PROTECTED) private final LinkedList<Component> components = new LinkedList<>();
    @Getter private final GameObjectType type;
    @Getter private final GameContext context;

    public GameObject(GameObjectType type, GameContext context) {
	this.type = type;
	this.context = context;
    }

    public void setId(int id) {
	if (this.id == -1) {
	    this.id = id;
	}
    }

    public synchronized final void addComponents(Component... a) {
	for (Component component : a) {
	    component.setContext(context);
	}
	this.components.addAll(Arrays.asList(a));
    }

    public final void message(Message message) {
	messages.add(message);
    }

    private synchronized GameObjectState getState() {
	ComponentStateBundle[][] states = new ComponentStateBundle[components.size()][];
	String[] classes = new String[components.size()];
	for (int i = 0; i < classes.length; i++) {
	    classes[i] = components.get(i).getClass().getName();
	    ArrayList<ComponentState> states1 = components.get(i).getStates();
	    ComponentStateBundle[] bundles = new ComponentStateBundle[states1.size()];
	    for (int j = 0; j < bundles.length; j++) {
		bundles[j] = new ComponentStateBundle(states1.get(j));
	    }
	    states[i] = bundles;
	}
	return new GameObjectState(states, classes);
    }

    public final synchronized ObjectBundle getBundle() {
	return new ObjectBundle(getState(), id, type);
    }

    public synchronized void process() {
	if (destroyed) {
	    return;
	}
	while (messages.size() > 0) {
	    Message message = messages.poll();
	    for (Component component : components) {
		component.processMessage(message);
	    }
	}
    }
}
