/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author white_oak
 */
@AllArgsConstructor public class Position {

    @Getter @Setter private int x, y;

    Position() {
    }
}
