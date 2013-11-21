package spaceisnear.server.objects;

import spaceisnear.game.bundles.ObjectBundle;
import spaceisnear.game.components.PlayerComponent;
import spaceisnear.game.components.PositionComponent;
import spaceisnear.game.objects.GameObjectType;
import spaceisnear.server.GameContext;

/**
 *
 * @author LPzhelud
 */
public class Player extends GameObject {

    public int connectionID;

    public Player(GameContext context, int connectionID) {
	super(GameObjectType.PLAYER, null);
	PositionComponent pc = new PositionComponent(24, 18);

	addComponents(pc, new PlayerComponent(pc));
	this.connectionID = connectionID;
    }
    

    public String getNickname() {
	return ((PlayerComponent) getComponents().getLast()).getNickname();
    }

    public void setNickname(String nickname) {
	((PlayerComponent) getComponents().getLast()).setNickname(nickname);
    }
}
