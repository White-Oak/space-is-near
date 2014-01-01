// Generated by delombok at Sun Dec 01 13:28:22 MSK 2013
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game;

import spaceisnear.game.objects.ClientGameObject;
import java.util.ArrayList;
import java.util.List;
import spaceisnear.AbstractGameObject;
import spaceisnear.Context;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.PaintableComponent;
import spaceisnear.game.layer.AtmosphericLayer;
import spaceisnear.game.layer.ObstaclesLayer;
import spaceisnear.game.messages.DirectedMessage;
import spaceisnear.game.messages.Message;

/**
 * @author LPzhelud
 */
public class GameContext extends Context {

    private final CameraMan cameraMan;
    private final ArrayList<PaintableComponent> paintables = new ArrayList<>();
    public static final int TILE_HEIGHT = 16;
    public static final int TILE_WIDTH = 16;
    private final Networking networking = new Networking(this);
    private final List<AbstractGameObject> objects;
    private int playerID = -1;
    private final Corev2 core;
    public static GameContext CONTEXT;

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

    @java.beans.ConstructorProperties({"cameraMan", "objects", "core"})
    public GameContext(CameraMan cameraMan, ArrayList<AbstractGameObject> objects, Corev2 core) {
	this.cameraMan = cameraMan;
	this.objects = objects;
	this.core = core;
	CONTEXT = this;
    }

    @Override
    public CameraMan getCameraMan() {
	return this.cameraMan;
    }

    public ArrayList<PaintableComponent> getPaintables() {
	return this.paintables;
    }

    public Networking getNetworking() {
	return this.networking;
    }

    @Override
    public List<AbstractGameObject> getObjects() {
	return this.objects;
    }

    public int getPlayerID() {
	return this.playerID;
    }

    public void setPlayerID(final int playerID) {
	this.playerID = playerID;
    }

    Corev2 getCore() {
	return this.core;
    }

    @Override
    public AtmosphericLayer getAtmosphere() {
	return getCameraMan().getAtmosphere();
    }

    @Override
    public ObstaclesLayer getObstacles() {
	return getCameraMan().getObstacles();
    }
}
