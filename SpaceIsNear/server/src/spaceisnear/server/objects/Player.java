package spaceisnear.server.objects;

import spaceisnear.game.components.VariablePropertiesComponent;
import lombok.Getter;
import spaceisnear.game.components.*;
import spaceisnear.game.components.inventory.InventoryComponent;
import spaceisnear.game.components.server.*;
import spaceisnear.game.objects.GameObjectType;
import spaceisnear.server.ServerContext;

/**
 * @author LPzhelud
 */
public class Player extends ServerGameObject {

    @Getter private final HealthComponent healthComponent;

    public Player(ServerContext context) {
	super(GameObjectType.PLAYER, context);
	PositionComponent pc = new PositionComponent(0, 0);
	healthComponent = new HealthComponent();
	addComponents(pc, healthComponent, new BreathingComponent(),
		new InventoryComponent(), new PlayerControllableComponent(), new NameComponent(),
		new VariablePropertiesComponent(), new PlayerChunkObserverComponent());
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
