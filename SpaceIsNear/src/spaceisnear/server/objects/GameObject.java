/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.server.objects;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import spaceisnear.game.bundles.ObjectBundle;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.PaintableComponent;
import spaceisnear.game.messages.Message;
import spaceisnear.game.objects.GameObjectState;
import spaceisnear.game.objects.GameObjectType;

/**
 *
 * @author White Oak
 */
public abstract class GameObject {

    private ConcurrentLinkedQueue<Message> messages = new ConcurrentLinkedQueue<>();
    @Getter private int id = -1;
    @Getter @Setter(AccessLevel.PROTECTED) private boolean destroyed = false;
    @Getter(AccessLevel.PROTECTED) private LinkedList<Component> components = new LinkedList<>();
    @Getter private final GameObjectType type;
    @Getter private final spaceisnear.game.GameContext context;

    public GameObject(GameObjectType type, spaceisnear.game.GameContext context) {
	this.type = type;
	this.context = context;
    }

    public void setId(int id) {
	if (this.id == -1) {
	    this.id = id;
	}
    }

    public synchronized final void addComponents(Component... a) {
	for (int i = 0; i < a.length; i++) {
	    Component component = a[i];
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

    private final synchronized GameObjectState getState() {
	return new GameObjectState(components.toArray(new Component[components.size()]), id, type);
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
