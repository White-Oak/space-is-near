package spaceisnear.server.objects;

import spaceisnear.game.bundles.ObjectBundle;
import spaceisnear.game.components.NameComponent;
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
	pc.setContext(context);

	addComponents(pc, new PlayerComponent(pc), new NameComponent(pc, null));
	this.connectionID = connectionID;
    }

    public String getNickname() {
	return ((NameComponent) getComponents().getLast()).getNickname();
    }

    public void setNickname(String nickname) {
	((NameComponent) getComponents().getLast()).setNickname(nickname);
    }
}
