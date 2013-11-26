/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.server.objects;

import spaceisnear.game.components.Component;
import spaceisnear.game.components.GamePlayerPositionComponent;
import spaceisnear.game.components.PositionComponent;

/**
 *
 * @author White Oak
 */
public class GamerPlayer extends spaceisnear.game.objects.Player {

    public GamerPlayer() {
	super(null);
	for (int i = 0; i < getComponents().size(); i++) {
	    Component component = getComponents().get(i);
	    if (component instanceof PositionComponent) {
		getComponents().set(i, new GamePlayerPositionComponent(((PositionComponent) component).getPosition(),
			this.getId()));
	    }
	}
    }
}
