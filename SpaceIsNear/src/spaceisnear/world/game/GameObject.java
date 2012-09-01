/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.world.game;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.*;
import spaceisnear.world.game.components.Component;
import spaceisnear.world.game.components.ComponentState;
import spaceisnear.world.game.components.PaintableComponent;
import spaceisnear.world.game.messages.Message;
import spaceisnear.world.game.messages.MessageTimePassed;

/**
 *
 * @author LPzhelud
 */
public abstract class GameObject {

    private ConcurrentLinkedQueue<Message> messages = new ConcurrentLinkedQueue<>();
    @Getter private final int id;
    @Getter @Setter(AccessLevel.PROTECTED) private boolean destroyed = false;
    @Getter private final GameObject parent;
    private LinkedList<Component> components = new LinkedList<>();
    @Getter private final GameObjectTypes type;
    private final GameContext context;

    public GameObject(int id, GameObject parent, GameObjectTypes type, GameContext context) {
	this.id = id;
	this.parent = parent;
	this.type = type;
	this.context = context;
    }

    public void addComponents(Component... a) {
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

    public void message(Message message) {
	messages.add(message);
    }

    public synchronized GameObjectState getState() {
	ArrayList<ComponentState> states = new ArrayList<>();
	for (Iterator<Component> it = components.iterator(); it.hasNext();) {
	    Component component = it.next();
	    states.add(component.getState());
	}
	return new GameObjectState(states, id, type);
    }

    public synchronized void process(int timeQuantsPassed) {
	if (destroyed) {
	    return;
	}
	message(new MessageTimePassed(timeQuantsPassed));
	while (messages.size() > 0) {
	    Message message = messages.poll();
	    for (Iterator<Component> it = components.iterator(); it.hasNext();) {
		Component component = it.next();
		component.processMessage(message);
	    }
	}
    }
}
