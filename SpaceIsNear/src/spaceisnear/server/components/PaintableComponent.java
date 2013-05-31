/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.server.components;

import spaceisnear.game.components.*;
import spaceisnear.game.GameContext;

public abstract class PaintableComponent extends Component {

    private final PositionComponent positionComponent;

    public PaintableComponent(PositionComponent positionComponent) {
	this.positionComponent = positionComponent;
    }

    public abstract void paintComponent(org.newdawn.slick.Graphics g);

    public final void paint(org.newdawn.slick.Graphics g) {
	int xto = positionComponent.getX() * GameContext.TILE_WIDTH;
	int yto = positionComponent.getY() * GameContext.TILE_HEIGHT;
	g.translate(xto, yto);
	paintComponent(g);
	g.translate(-xto, -yto);
    }
}
