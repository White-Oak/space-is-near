/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game;

import java.util.Iterator;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentState;
import spaceisnear.game.components.PlayerControllableComponent;
import spaceisnear.game.components.PositionComponent;

public class GamerPlayer extends Player {

    public GamerPlayer(int id, GameObject parent, GameContext context) {
	super(id, parent, context);
	addComponents(new PlayerControllableComponent());
    }

    public void updatePositionOnTheMap() {
	for (Iterator it = getComponents().iterator(); it.hasNext();) {
	    Component component = (Component) it.next();
	    if (component instanceof PositionComponent) {
		PositionComponent pc = (PositionComponent) component;
		getContext().getCamera().setGamerPosition(pc.getX(), pc.getY());
	    }
	}
    }

    @Override
    public synchronized void process() {
	super.process();
	updatePositionOnTheMap();
    }
    
}
