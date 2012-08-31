/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.world.game.components;

import java.awt.Graphics;
import spaceisnear.world.game.GameContext;
import spaceisnear.world.game.GameObject;

public abstract class PaintableComponent extends Component {

    private PositionComponent positionComponent;

    public PaintableComponent(GameContext context, GameObject owner, PositionComponent positionComponent) {
	super(context, owner);
	this.positionComponent = positionComponent;
    }

    public abstract void paintComponent(Graphics g);

    public void paint(Graphics g) {
	g.translate(positionComponent.getX(), positionComponent.getY());
	paintComponent(g);
	g.translate(-positionComponent.getX(), -positionComponent.getY());
    }
}
