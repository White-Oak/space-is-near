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
public abstract class Layer {

    @Getter @Setter(AccessLevel.PROTECTED) private int width, height;
    @Getter @Setter private int x, y;

    public Layer(int width, int height) {
	this.width = width;
	this.height = height;
    }

    public final void paintComponent(Graphics g) {
	paintLayer(g);
    }

    abstract void paintLayer(Graphics g);
}
