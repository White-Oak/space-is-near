package spaceisnear.server.objects;

import lombok.Getter;
import lombok.Setter;
import spaceisnear.game.components.*;
import spaceisnear.game.components.inventory.InventoryComponent;
import spaceisnear.game.components.server.*;
import spaceisnear.game.components.server.scriptprocessors.context.ServerContextMenu;
import spaceisnear.game.objects.GameObjectType;
import spaceisnear.server.ServerContext;

/**
 * @author LPzhelud
 */
public class Player extends ServerGameObject {

    @Getter private final HealthComponent healthComponent;
    @Getter @Setter private ServerContextMenu menu;

    public Player(ServerContext context) {
	super(GameObjectType.PLAYER, context);
	PositionComponent pc = new PositionComponent(0, 0);
	healthComponent = new HealthComponent();
	addComponents(pc, healthComponent, new BreathingComponent(),
		new InventoryComponent(), new PlayerControllableComponent(), new NameComponent(),
		new VariablePropertiesComponent());
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
