package spaceisnear.game.objects;

import spaceisnear.game.GameContext;
import spaceisnear.game.GameObjectTypes;
import spaceisnear.game.components.PlayerComponent;
import spaceisnear.game.components.PositionComponent;

/**
 *
 * @author LPzhelud
 */
public class Player extends GameObject {

    public Player(GameObject parent, GameContext context) {
	super(parent, GameObjectTypes.PLAYER, context);
	PositionComponent pc = new PositionComponent(24, 18);

	addComponents(pc, new PlayerComponent(pc));
    }
}
