// Generated by delombok at Sun Dec 01 13:28:22 MSK 2013
package spaceisnear.game.components;

import java.util.HashMap;
import lombok.*;
import spaceisnear.abstracts.AbstractGameObject;
import spaceisnear.abstracts.Context;
import spaceisnear.game.GameContext;
import spaceisnear.game.messages.Message;
import spaceisnear.game.objects.ClientGameObject;
import spaceisnear.game.ui.Position;

/**
 * @author LPzhelud
 */
@RequiredArgsConstructor public abstract class Component {

    @Getter private final HashMap<String, Object> states = new HashMap<>();
    @Getter private Context context = null;
    private final ComponentType type;
    @Getter @Setter private int ownerId = -1;
    private boolean animation, needsTime;
    @Setter
    @Getter
    private boolean dontProcess = false;

    public abstract void processMessage(Message message);

    public void setContext(Context context) {
	if (this.context == null) {
	    this.context = context;
	}
    }

    protected Object getStateValueNamed(String name) {
	return states.get(name);
    }

    protected void setStateValueNamed(String name, Object value) {
	states.put(name, value);
    }

    private AbstractGameObject cachedOwner;
    public AbstractGameObject getOwner() {
	assert getOwnerId() != -1 : getClass().getName();
	if (cachedOwner == null) {
	    cachedOwner = getContext().getObjects().get(getOwnerId());
	}
	return cachedOwner;
    }

    protected void addState(String name, Object value) {
	states.put(name, value);
    }

    public Position getPosition() {
	AbstractGameObject owner = getOwner();
	if (owner == null) {
	    return null;
	}
	return owner.getPosition();
    }

    protected PositionComponent getPositionComponent() {
	AbstractGameObject owner = getOwner();
	return owner.getPositionComponent();
    }

    public ComponentType getType() {
	return type;
    }

    protected void registerForAnimation() {
	if (!((ClientGameObject) getOwner()).isAnimated()) {
	    ((GameContext) context).addAnimated(getOwner());
	}
	animation = true;
    }

    protected void unregisterForAnimation() {
	animation = false;
	if (!((ClientGameObject) getOwner()).isAnimated()) {
	    ((GameContext) context).removeAnimated(getOwner());
	}
    }

    public boolean hasAnimation() {
	return animation;
    }

    public boolean needsTime() {
	return needsTime;
    }

    //

    public boolean isPaintable() {
	return false;
    }
}
