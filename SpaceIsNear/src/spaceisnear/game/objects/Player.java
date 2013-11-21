package spaceisnear.game.objects;

import spaceisnear.game.GameContext;
import spaceisnear.game.bundles.ObjectBundle;
import spaceisnear.game.components.PlayerComponent;
import spaceisnear.game.components.PositionComponent;

/**
 *
 * @author LPzhelud
 */
public class Player extends GameObject {

    public Player(GameContext context) {
	super(GameObjectType.PLAYER, context);
	PositionComponent pc = new PositionComponent(24, 18);

	addComponents(pc, new PlayerComponent(pc));
    }

    public String getNickname() {
	return ((PlayerComponent) getComponents().getLast()).getNickname();
    }

    public void setNickname(String nickname) {
	((PlayerComponent) getComponents().getLast()).setNickname(nickname);
    }

    public static Player getInstance(ObjectBundle bundle, GameContext context) {
	Player player = new Player(context);
	player.setId(bundle.getObjectID());
	player.addComponents(bundle.getState().getComponents());
	return player;
    }
}
