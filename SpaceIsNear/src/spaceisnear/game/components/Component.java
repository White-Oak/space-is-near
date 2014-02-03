// Generated by delombok at Sun Dec 01 13:28:22 MSK 2013
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import java.util.HashMap;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import spaceisnear.AbstractGameObject;
import spaceisnear.game.messages.Message;
import spaceisnear.Context;
import spaceisnear.game.objects.Position;

/**
 * @author LPzhelud
 */
@RequiredArgsConstructor public abstract class Component {

    @Getter private final HashMap<String, ComponentState> states = new HashMap<>();
    @Getter private Context context = null;
    private final ComponentType type;
    @Getter @Setter private int ownerId = -1;

    public abstract void processMessage(Message message);

    public void setContext(Context context) {
	if (this.context == null) {
	    this.context = context;
	}
    }

    protected ComponentState getStateNamed(String name) {
	return states.get(name);
    }

    protected Object getStateValueNamed(String name) {
	final ComponentState stateNamed = getStateNamed(name);
	if (stateNamed != null) {
	    return stateNamed.getValue();
	} else {
	    return null;
	}
    }

    protected AbstractGameObject getOwner() {
	if (getOwnerId() == -1) {
	    throw new RuntimeException("Owner id is -1");
	}
	final Context name = getContext();
	final List<AbstractGameObject> objects = name.getObjects();
	return objects.get(getOwnerId());
    }

    protected void addState(ComponentState state) {
	states.put(state.getName(), state);
    }

    public Position getPosition() {
	AbstractGameObject owner = getOwner();
	return owner.getPosition();
    }

    protected PositionComponent getPositionComponent() {
	AbstractGameObject owner = getOwner();
	return owner.getPositionComponent();
    }

    public ComponentType getType() {
	return type;
    }

}
