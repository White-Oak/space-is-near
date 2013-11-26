/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects;

import spaceisnear.game.GameContext;
import spaceisnear.game.bundles.ObjectBundle;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentState;
import spaceisnear.game.components.GamePlayerPositionComponent;
import spaceisnear.game.components.PlayerComponent;
import spaceisnear.game.components.PositionComponent;

public class GamerPlayer extends Player {

    public GamerPlayer(GameContext context) {
	super(context);
	for (int i = 0; i < getComponents().size(); i++) {
	    Component component = getComponents().get(i);
	    if (component instanceof PositionComponent) {
		getComponents().set(i, new GamePlayerPositionComponent(((PositionComponent) component).getPosition(),
			this.getId()));
	    }
	}
    }

    GamerPlayer() {
    }

    public static GamerPlayer getInstance(ObjectBundle bundle, GameContext context) {
	GamerPlayer player = new GamerPlayer();
	player.setContext(context);
	player.setId(bundle.getObjectID());
	Component[] components = bundle.getState().getComponents();
	Position p = null;
	for (int i = 0; i < components.length; i++) {
	    Component component = components[i];
	    if (component instanceof PositionComponent) {
		components[i] = new GamePlayerPositionComponent(((PositionComponent) component).getPosition(),
			player.getId());

		p = ((PositionComponent) component).getPosition();
	    }
	}
	for (Component component : components) {
	    if (component instanceof PlayerComponent) {
		((PlayerComponent) component).getStates().set(0, new ComponentState("position", p));
	    }
	}
	player.addComponents(components);
	return player;
    }
}
