// Generated by delombok at Sun Dec 01 13:28:22 MSK 2013
package spaceisnear.server.objects;

import spaceisnear.game.components.BreathingComponent;
import spaceisnear.game.components.HealthComponent;
import spaceisnear.game.components.NameComponent;
import spaceisnear.game.components.PlayerComponent;
import spaceisnear.game.components.PlayerControllableComponent;
import spaceisnear.game.components.PositionComponent;
import spaceisnear.game.components.inventory.InventoryComponent;
import spaceisnear.game.objects.GameObjectType;
import spaceisnear.server.ServerContext;

/**
 * @author LPzhelud
 */
public class Player extends ServerGameObject {

    public int connectionID;
    private final HealthComponent healthComponent;

    public Player(ServerContext context, int connectionID) {
	super(GameObjectType.PLAYER, context);
	PositionComponent pc = new PositionComponent(24, 18);
	healthComponent = new HealthComponent();
	addComponents(pc, new PlayerComponent(), healthComponent, new BreathingComponent(),
		new InventoryComponent(), new PlayerControllableComponent(), new NameComponent(null));
	this.connectionID = connectionID;
    }

    public String getNickname() {
	return ((NameComponent) getComponents().getLast()).getNickname();
    }

    public void setNickname(String nickname) {
	((NameComponent) getComponents().getLast()).setNickname(nickname);
    }

    public HealthComponent getHealthComponent() {
	return this.healthComponent;
    }
}
