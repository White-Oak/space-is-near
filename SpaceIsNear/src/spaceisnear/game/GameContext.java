/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game;

import java.util.ArrayList;
import lombok.*;
import spaceisnear.game.components.PaintableComponent;

/**
 *
 * @author LPzhelud
 */
@RequiredArgsConstructor public class GameContext {

    @Getter private final CameraMan camera;
    @Getter private ArrayList<PaintableComponent> paintables = new ArrayList<>();
    public static final int TILE_HEIGHT = 24, TILE_WIDTH = 16;

    public void addPaintable(PaintableComponent paintableComponent) {
	paintables.add(paintableComponent);
    }
}
