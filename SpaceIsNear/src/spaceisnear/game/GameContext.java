// Generated by delombok at Sun Dec 01 13:28:22 MSK 2013
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game;

import java.util.ArrayList;
import java.util.List;
import lombok.*;
import spaceisnear.abstracts.AbstractGameObject;
import spaceisnear.abstracts.Context;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.PositionComponent;
import spaceisnear.game.components.client.PaintableComponent;
import spaceisnear.game.messages.DirectedMessage;
import spaceisnear.game.messages.Message;
import spaceisnear.game.objects.*;
import spaceisnear.server.ServerContext;

/**
 * @author LPzhelud
 */
@RequiredArgsConstructor public class GameContext extends Context {

    @Getter private final CameraMan cameraMan;
    @Getter private final ArrayList<PaintableComponent> paintables = new ArrayList<>();
    public static final int TILE_HEIGHT = 32;
    public static final int TILE_WIDTH = 32;
    public static final int MAP_WIDTH = 64;
    public static final int MAP_HEIGHT = 64;
    public static float SCALING_X = 1f, SCALING_Y = 1f;
    @Getter private final List<AbstractGameObject> objects;
    @Getter @Setter private int playerID = -1;
    @Getter private final Corev2 core;
    public static final int HIDDEN_CLIENT_OBJECTS = 1;

    @Override
    public void sendThemAll(Message m) {
	if (!(m instanceof DirectedMessage)) {
	    for (AbstractGameObject gameObject : objects) {
		gameObject.message(m);
	    }
	}
    }

    @Override
    public void sendToID(Message m, int id) {
	objects.get(id).message(m);
    }

    public synchronized void addObject(ClientGameObject gameObject) {
	gameObject.setContext(this);
	objects.add(gameObject);
	for (Component component : gameObject.getComponents()) {
	    if (component instanceof PaintableComponent) {
		paintables.add((PaintableComponent) component);
	    }
	}
	gameObject.setId(objects.size() - 1);
    }

    public void checkSize() {
	while (objects.size() < ServerContext.HIDDEN_SERVER_OBJECTS) {
	    objects.add(null);
	}
    }

    public GamerPlayer getPlayer() {
	return (GamerPlayer) objects.get(playerID);
    }

    public void setCameraToPlayer() {
	GamerPlayer get = (GamerPlayer) objects.get(playerID);
	PositionComponent positionComponent = get.getPositionComponent();
	cameraMan.moveCameraToPlayer(positionComponent.getX(), positionComponent.getY());
    }

    public void setNewGamerPlayer(int playerID) {
	this.playerID = playerID;
	Player get = (Player) objects.get(playerID);
	final GamerPlayer gamerPlayer = new GamerPlayer(get);
	objects.set(playerID, gamerPlayer);
	core.newGamerPlayerReceived();
    }

    public boolean isLogined() {
	return core.getNetworking().isLogined();
    }

    public boolean isJoined() {
	return core.getNetworking().isJoined();
    }

}
