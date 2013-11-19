/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects;

import lombok.RequiredArgsConstructor;
import spaceisnear.game.components.Component;

/**
 *
 * @author LPzhelud
 */
@RequiredArgsConstructor public class GameObjectState {

    private final Component[] components;
    private final int id;
    private final GameObjectType type;
}
