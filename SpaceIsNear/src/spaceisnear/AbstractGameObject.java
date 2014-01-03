/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear;

import java.util.List;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentType;
import spaceisnear.game.components.PositionComponent;
import spaceisnear.game.messages.Message;
import spaceisnear.game.objects.GameObjectType;
import spaceisnear.game.objects.Position;

/**
 *
 * @author White Oak
 */
public abstract class AbstractGameObject {

    private PositionComponent positionComponent;

    public abstract void message(Message m);

    public abstract List<Component> getComponents();

    public abstract void process();

    public abstract Object getBundle();

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
}
