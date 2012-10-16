/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game;

import spaceisnear.game.objects.GameObject;
import spaceisnear.game.objects.Networking;
import java.util.ArrayList;
import java.util.Iterator;
import lombok.*;
import spaceisnear.game.components.PaintableComponent;
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
    private final ArrayList<GameObject> objects;

    public void addPaintable(PaintableComponent paintableComponent) {
	paintables.add(paintableComponent);
    }

    public void sendThemAll(Message m) {
	for (Iterator<GameObject> it = objects.iterator(); it.hasNext();) {
	    GameObject gameObject = it.next();
	    gameObject.message(m);
	}
    }
}
