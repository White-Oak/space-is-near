/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects;

import java.util.LinkedList;
import spaceisnear.game.GameContext;
import spaceisnear.game.bundles.ObjectBundle;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentType;
import spaceisnear.game.components.GamePlayerPositionComponent;
import spaceisnear.game.components.PositionComponent;

public class GamerPlayer extends Player {

    private GamerPlayer(GameContext context) {
	super(context);
    }

    public GamerPlayer(GameContext context, Player p) {
	this(context);
	setComponents(p.getComponents());
	for (int i = 0; i < getComponents().size(); i++) {
	    Component component = getComponents().get(i);
	    if (component.getType() == ComponentType.POSITION) {
		final Position position = ((PositionComponent) component).getPosition();
		final GamePlayerPositionComponent component1Component = new GamePlayerPositionComponent(position);
		component1Component.setContext(context);
		getComponents().set(i, component1Component);
	    }
	}
    }

    public static GamerPlayer getInstance(ObjectBundle bundle, GameContext context) {
	GamerPlayer player = new GamerPlayer(context);
	Component[] components = bundle.getState().getComponents(player.getId(), context);
	LinkedList<Component> list = new LinkedList<>();

	for (Component component : components) {
	    switch (component.getType()) {
		case POSITION:
		    component = new GamePlayerPositionComponent(((PositionComponent) component).getPosition());
		    component.setContext(context);
		case NAME:
		case INVENTORY:
		case PLAYER:
		    list.add(component);
		    break;
	    }
	}
	player.setComponents(list);
	player.setId(bundle.getObjectID());
	return player;
    }
}
