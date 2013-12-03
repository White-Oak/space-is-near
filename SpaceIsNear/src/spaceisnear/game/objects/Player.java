package spaceisnear.game.objects;

import java.util.LinkedList;
import spaceisnear.game.GameContext;
import spaceisnear.game.bundles.ObjectBundle;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentState;
import spaceisnear.game.components.GamePlayerPositionComponent;
import spaceisnear.game.components.NameComponent;
import spaceisnear.game.components.PlayerComponent;
import spaceisnear.game.components.PositionComponent;
import spaceisnear.game.components.inventory.InventoryComponent;

/**
 * Some player's object.
 *
 * @author LPzhelud
 */
public class Player extends GameObject {

    public Player(GameContext context) {
	super(GameObjectType.PLAYER, context);
	PositionComponent pc = new PositionComponent(24, 18, getId());

	addComponents(pc, new PlayerComponent(getId()), new NameComponent(null, getId()), new InventoryComponent(getId()));
    }

    public String getNickname() {
	return ((NameComponent) getComponents().get(getComponents().size() - 1)).getNickname();
    }

    public void setNickname(String nickname) {
	((NameComponent) getComponents().get(getComponents().size() - 1)).setNickname(nickname);
    }

    public static Player getInstance(ObjectBundle bundle, GameContext context) {
	Player player = new Player(context);
	player.setContext(context);
	player.setId(bundle.getObjectID());
	Component[] components = bundle.getState().getComponents(player.getId());
	LinkedList<Component> list = new LinkedList<>();
	for (Component component : components) {
	    if (component instanceof PositionComponent) {
		list.add(component);
	    } else if (component instanceof NameComponent || component instanceof InventoryComponent || component instanceof PlayerComponent) {
		list.add(component);
	    }
	}
	player.setComponents(list);
	return player;
    }
}
