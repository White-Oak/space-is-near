package spaceisnear.world.game;

import spaceisnear.world.game.components.PlayerComponent;
import spaceisnear.world.game.components.PositionComponent;

/**
 *
 * @author LPzhelud
 */
public class Player extends GameObject {

    public Player(int id, GameObject parent, GameContext context) {
	super(id, parent, GameObjectTypes.PLAYER, context);
	PositionComponent pc = new PositionComponent();
	addComponents(pc, new PlayerComponent(pc));
    }
}
