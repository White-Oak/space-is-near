/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 *
 * @author LPzhelud
 */
@AllArgsConstructor public class ComponentState {

    @Getter @Setter private String name;
    @Getter @Setter private Object value;
}
