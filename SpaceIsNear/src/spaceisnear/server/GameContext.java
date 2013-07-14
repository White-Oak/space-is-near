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

/**
 * @author LPzhelud
 */
@RequiredArgsConstructor public class GameContext {

	public static final int TILE_HEIGHT = 16, TILE_WIDTH = 16;
	@Getter private final Networking networking;
	@Getter private final List<GameObject> objects;
	private final List<GameObject> players = new LinkedList<>();

	public synchronized void sendThemAll(Message m) {
		for (Iterator<GameObject> it = objects.iterator(); it.hasNext(); ) {
			GameObject gameObject = it.next();
			gameObject.message(m);
		}
	}

	public synchronized void sendToID(Message m, int id) {
		objects.get(id).message(m);
	}

	public synchronized void addObject(GameObject gameObject) {
		objects.add(gameObject);
		gameObject.setId(objects.size() - 1);
	}

	public synchronized int addPlayer() {
		Player player = new Player(null, this);
		players.add(player);
		return players.size();
	}

	public Player getPlayer(int id) {
		return (Player) players.get(id);
	}
}
