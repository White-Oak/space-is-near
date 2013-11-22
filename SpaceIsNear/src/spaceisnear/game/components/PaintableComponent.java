/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import spaceisnear.game.GameContext;

public abstract class PaintableComponent extends Component {

    PaintableComponent() {
    }

    public PaintableComponent(PositionComponent positionComponent) {
	getStates().add(new ComponentState("positionComponent", positionComponent));
    }

    private PositionComponent getPositionComponent() {
	return (PositionComponent) getStates().get(0).getValue();
    }

    public abstract void paintComponent(org.newdawn.slick.Graphics g);

    public final void paint(org.newdawn.slick.Graphics g) {
	int xto = getPositionComponent().getX() * GameContext.TILE_WIDTH;
	int yto = getPositionComponent().getY() * GameContext.TILE_HEIGHT;
	g.translate(xto, yto);
	paintComponent(g);
	g.translate(-xto, -yto);
    }
}
