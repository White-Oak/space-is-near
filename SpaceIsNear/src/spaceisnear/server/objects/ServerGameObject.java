// Generated by delombok at Sun Dec 01 13:28:22 MSK 2013
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.server.objects;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import spaceisnear.AbstractGameObject;
import spaceisnear.Context;
import spaceisnear.game.bundles.ObjectBundle;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentState;
import spaceisnear.game.components.ComponentStateBundle;
import spaceisnear.game.messages.Message;
import spaceisnear.game.objects.GameObjectState;
import spaceisnear.game.objects.GameObjectType;
import spaceisnear.server.ServerContext;

/**
 * @author White Oak
 */
public abstract class ServerGameObject extends AbstractGameObject {

    private final ConcurrentLinkedQueue<Message> messages = new ConcurrentLinkedQueue<>();
    private int id = -1;
    private boolean destroyed = false;
    private final LinkedList<Component> components = new LinkedList<>();
    private final GameObjectType type;
    private final ServerContext context;

    public ServerGameObject(GameObjectType type, ServerContext context) {

	this.type = type;
	this.context = context;
    }

    public void setId(int id) {
	if (this.id == -1) {
	    this.id = id;
	    for (Component component : components) {
		component.setOwnerId(id);
	    }
	}
    }

    public final synchronized void addComponents(Component... a) {
	for (Component component : a) {
	    component.setContext(context);
	}
	this.components.addAll(Arrays.asList(a));
    }

    @Override
    public final void message(Message message) {
	messages.add(message);
    }

    private synchronized GameObjectState getState() {
	ComponentStateBundle[][] states = new ComponentStateBundle[components.size()][];
	String[] classes = new String[components.size()];
	for (int i = 0; i < classes.length; i++) {
	    classes[i] = components.get(i).getClass().getName();
	    HashMap<String, ComponentState> states1 = components.get(i).getStates();
	    ComponentStateBundle[] bundles = new ComponentStateBundle[states1.size()];
	    Collection<ComponentState> values = states1.values();
	    int j = 0;
	    for (ComponentState componentState : values) {
		bundles[j] = new ComponentStateBundle(componentState);
		j++;
	    }
	    states[i] = bundles;
	}
	return new GameObjectState(states, classes);
    }

    @Override
    public final synchronized ObjectBundle getBundle() {
	return new ObjectBundle(getState(), id, type);
    }

    @Override
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

    public int getId() {
	return this.id;
    }

    public boolean isDestroyed() {
	return this.destroyed;
    }

    protected void setDestroyed(final boolean destroyed) {
	this.destroyed = destroyed;
    }

    @Override
    public LinkedList<Component> getComponents() {
	return this.components;
    }

    public GameObjectType getType() {
	return this.type;
    }

    @Override
    public Context getContext() {
	return this.context;
    }
}