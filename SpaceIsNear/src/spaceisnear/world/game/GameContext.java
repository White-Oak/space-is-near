/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.world.game;

import java.util.ArrayList;
import lombok.*;
import spaceisnear.world.game.components.PaintableComponent;

/**
 *
 * @author LPzhelud
 */
@RequiredArgsConstructor public class GameContext {

    @Getter private final GameMap map;
    public static final int YEAR_TIME = 600;
    @Getter private ArrayList<PaintableComponent> paintables = new ArrayList<>();

    public void addPaintable(PaintableComponent paintableComponent) {
	paintables.add(paintableComponent);
    }
}
