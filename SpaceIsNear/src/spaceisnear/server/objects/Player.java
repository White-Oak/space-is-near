package spaceisnear.server.objects;

import spaceisnear.game.objects.GameObjectType;
import spaceisnear.server.GameContext;
import spaceisnear.server.components.PlayerComponent;
import spaceisnear.server.components.PositionComponent;

/**
 *
 * @author LPzhelud
 */
public class Player extends GameObject {

    private int connectionID;

    public Player(GameObject parent, GameContext context, int connectionID) {
	super(parent, GameObjectType.PLAYER, context);
	PositionComponent pc = new PositionComponent(24, 18);

	addComponents(pc, new PlayerComponent(pc));
	this.connectionID = connectionID;
    }
}
