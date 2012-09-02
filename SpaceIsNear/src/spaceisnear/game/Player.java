package spaceisnear.game;

import spaceisnear.game.components.PlayerComponent;
import spaceisnear.game.components.PositionComponent;

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
