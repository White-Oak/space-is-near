/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.abstracts;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.*;
import spaceisnear.game.components.*;
import spaceisnear.game.components.server.VariablePropertiesComponent;
import spaceisnear.game.messages.*;
import spaceisnear.game.objects.*;

/**
 *
 * @author White Oak
 */
public abstract class AbstractGameObject {

    @Setter @Getter private ConcurrentLinkedQueue<Message> messages = new ConcurrentLinkedQueue<>();
    private PositionComponent positionComponent;
    private VariablePropertiesComponent properties;
    @Getter @Setter private boolean destroyed = false;
    @Getter @Setter(AccessLevel.PROTECTED) private List<Component> components = new ArrayList<>();

    public final void message(Message message) {
	messages.add(message);
    }

    public synchronized void process() {
	if (destroyed) {
	    return;
	}
	int savedSize = messages.size();
	for (int i = 0; i < savedSize; i++) {
	    Message message = messages.poll();
	    components.forEach(component -> {
		if (message.getMessageType() != MessageType.TIME_PASSED) {
		    component.processMessage(message);
		}
	    });
	}
    }

    public abstract Context getContext();

    public abstract GameObjectType getType();

    public Position getPosition() {
	return getPositionComponent().getPosition();
    }

    public PositionComponent getPositionComponent() {
	if (positionComponent == null) {
	    for (Component component : getComponents()) {
		if (component.getType() == ComponentType.POSITION || component.getType() == ComponentType.GAMER_PLAYER_POSITION) {
		    positionComponent = (PositionComponent) component;
		}
	    }
	}
	return positionComponent;
    }

    public VariablePropertiesComponent getVariablePropertiesComponent() {
	if (properties == null) {
	    for (Component component : getComponents()) {
		if (component.getType() == ComponentType.VARIABLES) {
		    properties = (VariablePropertiesComponent) component;
		}
	    }
	}
	return properties;
    }

    public abstract int getId();
}
