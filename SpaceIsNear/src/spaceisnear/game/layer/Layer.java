/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.layer;

import java.awt.Graphics;
import lombok.*;
import spaceisnear.game.components.PaintableComponent;
import spaceisnear.game.components.PositionComponent;

/**
 *
 * @author LPzhelud
 */
public abstract class Layer extends PaintableComponent {

    @Getter @Setter(AccessLevel.PROTECTED) private int width, height;
    @Getter @Setter private int x, y;

    public Layer(PositionComponent positionComponent, int width, int height) {
	super(positionComponent);
	this.width = width;
	this.height = height;
    }

    @Override
    public final void paintComponent(org.newdawn.slick.Graphics g) {
	paintLayer(g);
    }

    abstract void paintLayer(org.newdawn.slick.Graphics g);
}
