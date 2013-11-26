/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.*;
import spaceisnear.game.GameContext;
import spaceisnear.game.bundles.ObjectBundle;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentState;
import spaceisnear.game.components.ComponentStateBundle;
import spaceisnear.game.components.PaintableComponent;
import spaceisnear.game.messages.Message;

/**
 *
 * @author LPzhelud
 */
public abstract class GameObject {

    private final ConcurrentLinkedQueue<Message> messages = new ConcurrentLinkedQueue<>();
    @Getter private int id = -1;
    @Getter @Setter(AccessLevel.PROTECTED) private boolean destroyed = false;
    @Getter private final LinkedList<Component> components = new LinkedList<>();
    @Getter private final GameObjectType type;
    @Getter @Setter(AccessLevel.PROTECTED) private GameContext context;

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
	    if (component instanceof PaintableComponent) {
		context.addPaintable((PaintableComponent) component);
	    }
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
		bundles[j] = new ComponentStateBundle(states1.get(i));
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
	    for (Iterator<Component> it = components.iterator(); it.hasNext();) {
		Component component = it.next();
		component.processMessage(message);
	    }
	}
    }
}
