/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import spaceisnear.game.components.Component;

/**
 *
 * @author LPzhelud
 */
@RequiredArgsConstructor public class GameObjectState {

    @Getter(AccessLevel.PACKAGE) private final Component[] components;
}
