/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects;

import java.util.LinkedList;
import spaceisnear.game.GameContext;
import spaceisnear.game.bundles.ObjectBundle;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.GamePlayerPositionComponent;
import spaceisnear.game.components.NameComponent;
import spaceisnear.game.components.PlayerComponent;
import spaceisnear.game.components.PositionComponent;
import spaceisnear.game.components.inventory.InventoryComponent;

public class GamerPlayer extends Player {

    private GamerPlayer(GameContext context) {
	super(context);
	for (int i = 0; i < getComponents().size(); i++) {
	    Component component = getComponents().get(i);
	    if (component instanceof PositionComponent) {
		final Position position = ((PositionComponent) component).getPosition();
		getComponents().set(i, new GamePlayerPositionComponent(position, this.getId()));
	    }
	}
    }

    public static GamerPlayer getInstance(ObjectBundle bundle, GameContext context) {
	GamerPlayer player = new GamerPlayer(context);
	player.setId(bundle.getObjectID());
	Component[] components = bundle.getState().getComponents(player.getId());
	LinkedList<Component> list = new LinkedList<>();
	for (Component component : components) {
	    if (component instanceof PositionComponent) {
		component = new GamePlayerPositionComponent(((PositionComponent) component).getPosition(), player.getId());
		list.add(component);
	    } else if (component instanceof NameComponent || component instanceof InventoryComponent || component instanceof PlayerComponent) {
		list.add(component);
	    }
	}
	player.setComponents(list);
	return player;
    }
}
