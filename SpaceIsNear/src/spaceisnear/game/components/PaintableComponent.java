/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import spaceisnear.game.GameContext;
import spaceisnear.game.objects.Position;

public abstract class PaintableComponent extends Component {

    PaintableComponent() {
    }

    public PaintableComponent(PositionComponent positionComponent) {
	getStates().add(new ComponentState("position", positionComponent.getPosition()));
    }

    public int getX() {
	return ((Position) getStates().get(0).getValue()).getX();
    }

    public int getY() {
	return ((Position) getStates().get(0).getValue()).getY();
    }

    public abstract void paintComponent(org.newdawn.slick.Graphics g);

    public final void paint(org.newdawn.slick.Graphics g) {
	int xto = getX() * GameContext.TILE_WIDTH;
	int yto = getY() * GameContext.TILE_HEIGHT;
	g.translate(xto, yto);
	paintComponent(g);
	g.translate(-xto, -yto);
    }
}
