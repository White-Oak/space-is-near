/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.*;
import spaceisnear.game.GameContext;
import spaceisnear.game.GameObjectState;
import spaceisnear.game.GameObjectTypes;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentState;
import spaceisnear.game.components.PaintableComponent;
import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessageTimePassed;

/**
 *
 * @author LPzhelud
 */
public abstract class GameObject {

    private ConcurrentLinkedQueue<Message> messages = new ConcurrentLinkedQueue<>();
    @Getter private int id = -1;
    @Getter @Setter(AccessLevel.PROTECTED) private boolean destroyed = false;
    @Getter private final GameObject parent;
    @Getter(AccessLevel.PROTECTED) private LinkedList<Component> components = new LinkedList<>();
    @Getter private final GameObjectTypes type;
    @Getter private final GameContext context;

    public GameObject(GameObject parent, GameObjectTypes type, GameContext context) {
	this.parent = parent;
	this.type = type;
	this.context = context;
    }

    public void setId(int id) {
	if (this.id == -1) {
	    this.id = id;
	}
    }

    public final void addComponents(Component... a) {
	for (int i = 0; i < a.length; i++) {
	    Component component = a[i];
	    component.setContext(context);
	    component.setOwner(this);
	    if (component instanceof PaintableComponent) {
		context.addPaintable((PaintableComponent) component);
	    }
	}
	this.components.addAll(Arrays.asList(a));
    }

    public final void message(Message message) {
	messages.add(message);
    }

    public final synchronized GameObjectState getState() {
	ArrayList<ComponentState> states = new ArrayList<>();
	for (Iterator<Component> it = components.iterator(); it.hasNext();) {
	    Component component = it.next();
	    states.add(component.getState());
	}
	return new GameObjectState(states, id, type);
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
