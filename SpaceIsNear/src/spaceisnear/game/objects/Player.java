package spaceisnear.game.objects;

import java.util.LinkedList;
import spaceisnear.game.GameContext;
import spaceisnear.game.bundles.ObjectBundle;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.NameComponent;
import spaceisnear.game.components.PlayerComponent;
import spaceisnear.game.components.PositionComponent;
import spaceisnear.game.components.inventory.InventoryComponent;

/**
 * Some player's object.
 *
 * @author LPzhelud
 */
public class Player extends ClientGameObject {

    public Player(GameContext context) {
	super(GameObjectType.PLAYER, context);
	PositionComponent pc = new PositionComponent(24, 18);

	addComponents(pc, new PlayerComponent(), new NameComponent(null), new InventoryComponent());
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
	Component[] components = bundle.getState().getComponents(bundle.getObjectID(), context);
	LinkedList<Component> list = new LinkedList<>();
	for (Component component : components) {
	    switch (component.getType()) {
		case POSITION:
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
