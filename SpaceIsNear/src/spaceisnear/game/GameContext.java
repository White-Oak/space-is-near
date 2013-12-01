// Generated by delombok at Sun Dec 01 13:28:22 MSK 2013
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game;

import spaceisnear.game.objects.GameObject;
import java.util.ArrayList;
import spaceisnear.Context;
import spaceisnear.game.components.PaintableComponent;
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
    private final ArrayList<GameObject> objects;
    private int playerID = -1;
    private final Corev2 core;

    public void addPaintable(PaintableComponent paintableComponent) {
	paintables.add(paintableComponent);
    }

    @Override
    public void sendThemAll(Message m) {
	if (!(m instanceof DirectedMessage)) {
	    for (GameObject gameObject : objects) {
		gameObject.message(m);
	    }
	}
    }

    @Override
    public void sendToID(Message m, int id) {
	objects.get(id).message(m);
    }

    public synchronized void addObject(GameObject gameObject) {
	objects.add(gameObject);
	gameObject.setId(objects.size() - 1);
    }

    @java.beans.ConstructorProperties({"cameraMan", "objects", "core"})
    public GameContext(final CameraMan cameraMan, final ArrayList<GameObject> objects, final Corev2 core) {
	this.cameraMan = cameraMan;
	this.objects = objects;
	this.core = core;
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

    public ArrayList<GameObject> getObjects() {
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
}
