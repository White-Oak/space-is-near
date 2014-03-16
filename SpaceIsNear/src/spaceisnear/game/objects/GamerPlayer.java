/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects;

import spaceisnear.game.components.*;
import spaceisnear.game.components.client.GamePlayerPositionComponent;

public class GamerPlayer extends Player {

    private GamerPlayer() {
	super(GameObjectType.GAMER_PLAYER);
    }

    public GamerPlayer(Player p) {
	setComponents(p.getComponents());
	for (int i = 0; i < getComponents().size(); i++) {
	    Component component = getComponents().get(i);
	    if (component.getType() == ComponentType.POSITION) {
		final Position position = ((PositionComponent) component).getPosition();
		final GamePlayerPositionComponent newPositionComponent = new GamePlayerPositionComponent(position);
		System.out.println("Position of gamer player is " + position);
		getComponents().set(i, newPositionComponent);
	    }
	}
	setContext(p.getContext());
	setId(p.getId());
	setMessages(p.getMessages());
    }
}
