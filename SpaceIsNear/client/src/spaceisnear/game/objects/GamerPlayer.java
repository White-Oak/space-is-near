/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects;

import lombok.NonNull;
import spaceisnear.game.CameraMan;
import spaceisnear.game.GameContext;
import spaceisnear.game.components.*;
import spaceisnear.game.components.client.GamePlayerPositionComponent;
import spaceisnear.game.ui.Position;

public class GamerPlayer extends Player {

    private GamerPlayer() {
	super(GameObjectType.GAMER_PLAYER);
    }

    public GamerPlayer(@NonNull Player p) {
	this();
	setComponents(p.getComponents());
	for (int i = 0; i < getComponents().size(); i++) {
	    Component component = getComponents().get(i);
	    if (component.getType() == ComponentType.POSITION) {
		final Position position = getPosition();
		final GamePlayerPositionComponent newPositionComponent = new GamePlayerPositionComponent(position);
		getComponents().set(i, newPositionComponent);
		final GameContext context = p.getContext();
		@NonNull final CameraMan cameraMan = context.getEngine().getCore().getCameraMan();
		assert cameraMan != null;
		cameraMan.moveCameraToPlayer(position.getX(), position.getY());
	    }
	}
	setContext(p.getContext());
	setId(p.getId());
	setMessages(p.getMessages());
    }
}
