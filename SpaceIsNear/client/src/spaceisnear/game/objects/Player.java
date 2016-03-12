package spaceisnear.game.objects;

import spaceisnear.game.components.*;
import spaceisnear.game.components.client.PlayerComponent;
import spaceisnear.game.components.inventory.InventoryComponent;

/**
 * Some player's object.
 *
 * @author LPzhelud
 */
public class Player extends ClientGameObject {

    public Player() {
	this(GameObjectType.PLAYER);
    }

    protected Player(GameObjectType type) {
	super(type);
	addComponents(new PlayerComponent(), new NameComponent(), new PositionComponent(0, 0), new InventoryComponent());
    }

    public String getNickname() {
	return getNameComponent().getNickname();
    }

    public void setNickname(String nickname) {
	getNameComponent().setNickname(nickname);
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

}
