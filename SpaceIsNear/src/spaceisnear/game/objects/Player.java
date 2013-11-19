package spaceisnear.game.objects;

import spaceisnear.game.GameContext;
import spaceisnear.game.components.PlayerComponent;
import spaceisnear.game.components.PositionComponent;

/**
 *
 * @author LPzhelud
 */
public class Player extends GameObject {

    public Player( GameContext context) {
	super(GameObjectType.PLAYER, context);
	PositionComponent pc = new PositionComponent(24, 18);

	addComponents(pc, new PlayerComponent(pc));
    }
}
