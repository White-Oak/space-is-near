/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import spaceisnear.game.messages.Message;
import spaceisnear.server.objects.GameObject;
import spaceisnear.server.objects.Player;

import java.util.*;
import spaceisnear.Context;
import spaceisnear.game.CameraMan;
import spaceisnear.game.layer.TiledLayer;
import spaceisnear.game.messages.DirectedMessage;

/**
 * @author LPzhelud
 */
@RequiredArgsConstructor public class GameContext extends Context {

    public static final int TILE_HEIGHT = 16, TILE_WIDTH = 16;
    @Getter private final Networking networking;
    @Getter private final List<GameObject> objects;
    @Getter private final List<Player> players = new LinkedList<>();
    @Getter private final TiledLayer tiledLayer;

    @Override
    public synchronized void sendThemAll(Message m) {
	if (!(m instanceof DirectedMessage)) {
	    for (GameObject gameObject : objects) {
		gameObject.message(m);
	    }
	}
    }

    @Override
    public synchronized void sendToID(Message m, int id) {
	objects.get(id).message(m);
    }

    public synchronized void addObject(GameObject gameObject) {
	objects.add(gameObject);
	gameObject.setId(objects.size() - 1);
    }

    public synchronized Player addPlayer(int connectionID) {
	Player player = new Player(this, connectionID);
	players.add(player);
	addObject(player);
	return player;
    }

    public Player getPlayer(int id) {
	return (Player) players.get(id);
    }

    @Override
    public CameraMan getCameraMan() {
	return null;
    }
}
