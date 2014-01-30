/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects;

import spaceisnear.game.GameContext;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentType;
import spaceisnear.game.components.client.GamePlayerPositionComponent;
import spaceisnear.game.components.PositionComponent;

public class GamerPlayer extends Player {

    private GamerPlayer(GameContext context) {
	super(context, GameObjectType.GAMER_PLAYER);
    }

    public GamerPlayer(GameContext context, Player p) {
	this(context);
	setComponents(p.getComponents());
	for (int i = 0; i < getComponents().size(); i++) {
	    Component component = getComponents().get(i);
	    if (component.getType() == ComponentType.POSITION) {
		final Position position = ((PositionComponent) component).getPosition();
		final GamePlayerPositionComponent newPositionComponent = new GamePlayerPositionComponent(position);
		newPositionComponent.setContext(context);
		getComponents().set(i, newPositionComponent);
	    }
	}
    }
}
