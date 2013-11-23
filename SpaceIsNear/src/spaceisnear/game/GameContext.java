/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game;

import spaceisnear.game.objects.GameObject;
import java.util.ArrayList;
import java.util.Iterator;
import lombok.*;
import spaceisnear.game.components.PaintableComponent;
import spaceisnear.game.messages.DirectedMessage;
import spaceisnear.game.messages.Message;

/**
 *
 * @author LPzhelud
 */
@RequiredArgsConstructor public class GameContext {

    @Getter private final CameraMan camera;
    @Getter private ArrayList<PaintableComponent> paintables = new ArrayList<>();
    public static final int TILE_HEIGHT = 16, TILE_WIDTH = 16;
    @Getter private Networking networking = new Networking(this);
    @Getter private final ArrayList<GameObject> objects;
    @Getter @Setter private int playerID = -1;
    @Getter(AccessLevel.PACKAGE) private final Corev2 core;

    public void addPaintable(PaintableComponent paintableComponent) {
	paintables.add(paintableComponent);
    }

    public void sendThemAll(Message m) {
	if (!(m instanceof DirectedMessage)) {
	    for (Iterator<GameObject> it = objects.iterator(); it.hasNext();) {
		GameObject gameObject = it.next();
		gameObject.message(m);
	    }
	}
    }

    public void sendToID(Message m, int id) {
	objects.get(id).message(m);
    }

    public synchronized void addObject(GameObject gameObject) {
	objects.add(gameObject);
	gameObject.setId(objects.size() - 1);
    }
}
