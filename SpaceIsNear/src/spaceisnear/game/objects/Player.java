package spaceisnear.game.objects;

import spaceisnear.game.GameContext;
import spaceisnear.game.bundles.ObjectBundle;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentState;
import spaceisnear.game.components.NameComponent;
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

	addComponents(pc, new PlayerComponent(pc), new NameComponent(pc, null));
    }

    Player() {
	super(GameObjectType.PLAYER, null);
    }

    public String getNickname() {
	return ((NameComponent) getComponents().getLast()).getNickname();
    }

    public void setNickname(String nickname) {
	((NameComponent) getComponents().getLast()).setNickname(nickname);
    }

    public static Player getInstance(ObjectBundle bundle, GameContext context) {
	Player player = new Player();
	player.setContext(context);
	player.setId(bundle.getObjectID());
	Component[] components = bundle.getState().getComponents();
	Position p = null;
	for (int i = 0; i < components.length; i++) {
	    Component component = components[i];
	    if (component instanceof PositionComponent) {
		p = ((PositionComponent) component).getPosition();
	    }
	}
	for (int i = 0; i < components.length; i++) {
	    Component component = components[i];
	    if (component instanceof PlayerComponent) {
		((PlayerComponent) component).getStates().set(0, new ComponentState("position", p));
	    }
	}
	player.addComponents(components);
	return player;
    }
}
