/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.layer;

import java.awt.Graphics;
import lombok.*;

/**
 *
 * @author LPzhelud
 */
@RequiredArgsConstructor public abstract class Layer {

    @Getter @Setter(AccessLevel.PROTECTED) @NonNull private int width, height;
    @Getter @Setter private int x, y;

    public abstract void paint(Graphics g);
}
