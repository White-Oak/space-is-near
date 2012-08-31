/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.world.game;

import spaceisnear.world.game.components.ComponentState;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author LPzhelud
 */
@AllArgsConstructor public class GameObjectState {

    @Getter private final ArrayList<ComponentState> states;
    @Getter private final int id;
    @Getter private final GameObjectTypes type;
}
