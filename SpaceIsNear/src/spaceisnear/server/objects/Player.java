package spaceisnear.server.objects;

import lombok.Getter;
import spaceisnear.game.components.BreathingComponent;
import spaceisnear.game.components.HealthComponent;
import spaceisnear.game.components.NameComponent;
import spaceisnear.game.components.PlayerComponent;
import spaceisnear.game.components.PositionComponent;
import spaceisnear.game.components.inventory.InventoryComponent;
import spaceisnear.game.objects.GameObjectType;
import spaceisnear.server.GameContext;

/**
 *
 * @author LPzhelud
 */
public class Player extends GameObject {

    public int connectionID;
    @Getter private final HealthComponent healthComponent;

    public Player(GameContext context, int connectionID) {
	super(GameObjectType.PLAYER, null);
	PositionComponent pc = new PositionComponent(24, 18);
	pc.setContext(context);

	healthComponent = new HealthComponent();
	addComponents(pc, new PlayerComponent(pc), new NameComponent(pc, null), healthComponent,
		new BreathingComponent(), new InventoryComponent());
	this.connectionID = connectionID;
    }

    public String getNickname() {
	return ((NameComponent) getComponents().getLast()).getNickname();
    }

    public void setNickname(String nickname) {
	((NameComponent) getComponents().getLast()).setNickname(nickname);
    }
}
