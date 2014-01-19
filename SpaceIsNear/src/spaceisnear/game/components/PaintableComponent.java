/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import spaceisnear.game.GameContext;

public abstract class PaintableComponent extends Component {

    protected PaintableComponent(ComponentType type) {
	super(type);
    }

    public int getX() {
	return getPosition().getX();
    }

    public int getY() {
	return getPosition().getY();
    }

    public int getDelayX() {
	return getPositionComponent().getDelayX();
    }

    public int getDelayY() {
	return getPositionComponent().getDelayY();
    }

    public abstract void paintComponent(org.newdawn.slick.Graphics g);

    public final void paint(org.newdawn.slick.Graphics g) {
	if (getContext().getCameraMan().belongsToCamera(getPosition())) {
	    int xto = getX() * GameContext.TILE_WIDTH - getDelayX();
	    int yto = getY() * GameContext.TILE_HEIGHT - getDelayY();
	    g.translate(xto, yto);
	    paintComponent(g);
	    g.translate(-xto, -yto);
	}
    }
}
