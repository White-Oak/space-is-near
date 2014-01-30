package spaceisnear.game.objects;

import java.util.LinkedList;
import spaceisnear.game.GameContext;
import spaceisnear.game.bundles.ObjectBundle;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentType;
import spaceisnear.game.components.NameComponent;
import spaceisnear.game.components.client.PlayerComponent;
import spaceisnear.game.components.PositionComponent;
import spaceisnear.game.components.inventory.InventoryComponent;

/**
 * Some player's object.
 *
 * @author LPzhelud
 */
public class Player extends ClientGameObject {

    protected Player(GameContext context) {
	super(GameObjectType.PLAYER, context);
    }

    protected Player(GameContext context, GameObjectType type) {
	super(type, context);
    }

    public String getNickname() {
	return getNameComponent().getNickname();
    }

    private NameComponent getNameComponent() {
	for (Component component : getComponents()) {
	    if (component.getType() == ComponentType.NAME) {
		return (NameComponent) component;
	    }
	}
	return null;
    }

    public void setNickname(String nickname) {
	getNameComponent().setNickname(nickname);
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
	player.addComponents(new PlayerComponent());
	player.setId(bundle.getObjectID());
	return player;
    }
}
