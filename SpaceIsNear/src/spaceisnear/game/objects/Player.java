package spaceisnear.game.objects;

import spaceisnear.game.GameContext;
import spaceisnear.game.GameObjectTypes;
import spaceisnear.game.components.PlayerComponent;
import spaceisnear.game.components.PlayerControllableComponent;
import spaceisnear.game.components.PositionComponent;
import spaceisnear.game.messages.MessageTeleported;

/**
 *
 * @author LPzhelud
 */
public class Player extends GameObject {
    
    public Player(int id, GameObject parent, GameContext context) {
	super(id, parent, GameObjectTypes.PLAYER, context);
	PositionComponent pc = new PositionComponent(24,18);
	
	addComponents(pc, new PlayerComponent(pc));
    }
}
