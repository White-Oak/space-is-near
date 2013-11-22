/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import spaceisnear.game.components.Component;

/**
 *
 * @author LPzhelud
 */
@AllArgsConstructor public class GameObjectState {

    @Getter(AccessLevel.PACKAGE) private Component[] components;

    public GameObjectState() {
    }
}
