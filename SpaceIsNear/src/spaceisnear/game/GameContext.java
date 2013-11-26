/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game;

import spaceisnear.game.objects.GameObject;
import java.util.ArrayList;
import lombok.*;
import spaceisnear.Context;
import spaceisnear.game.components.PaintableComponent;
import spaceisnear.game.messages.DirectedMessage;
import spaceisnear.game.messages.Message;
import spaceisnear.game.layer.TiledLayer;

/**
 *
 * @author LPzhelud
 */
@RequiredArgsConstructor public class GameContext extends Context {

    @Getter private final CameraMan camera;
    @Getter private final ArrayList<PaintableComponent> paintables = new ArrayList<>();
    public static final int TILE_HEIGHT = 16, TILE_WIDTH = 16;
    @Getter private final Networking networking = new Networking(this);
    @Getter private final ArrayList<GameObject> objects;
    @Getter @Setter private int playerID = -1;
    @Getter(AccessLevel.PACKAGE) private final Corev2 core;

    public void addPaintable(PaintableComponent paintableComponent) {
	paintables.add(paintableComponent);
    }

    public void sendThemAll(Message m) {
	if (!(m instanceof DirectedMessage)) {
	    for (GameObject gameObject : objects) {
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

    @Override
    public TiledLayer getTiledLayer() {
	return camera.getTiledLayer();
    }
}
