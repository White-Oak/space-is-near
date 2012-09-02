/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import java.awt.Graphics;
import spaceisnear.game.GameContext;
import spaceisnear.game.GameObject;

public abstract class PaintableComponent extends Component {

    private final PositionComponent positionComponent;

    public PaintableComponent(PositionComponent positionComponent) {
	this.positionComponent = positionComponent;
    }

    public abstract void paintComponent(Graphics g);

    public final void paint(Graphics g) {
	g.translate(positionComponent.getX(), positionComponent.getY());
	paintComponent(g);
	g.translate(-positionComponent.getX(), -positionComponent.getY());
    }
}
