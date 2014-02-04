package spaceisnear.game.objects;

import spaceisnear.game.GameContext;
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

    public Player(GameContext context) {
	this(context, GameObjectType.PLAYER);
    }

    protected Player(GameContext context, GameObjectType type) {
	super(type, context);
	addComponents(new PlayerComponent(), new NameComponent(), new PositionComponent(0, 0), new InventoryComponent());
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

    public InventoryComponent getInventoryComponent() {
	for (Component component : getComponents()) {
	    if (component.getType() == ComponentType.INVENTORY) {
		return (InventoryComponent) component;
	    }
	}
	return null;
    }

    public void setNickname(String nickname) {
	getNameComponent().setNickname(nickname);
    }
}
