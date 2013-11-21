/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import spaceisnear.game.GameContext;
import spaceisnear.game.messages.Message;

/**
 *
 * @author LPzhelud
 */
public class PlayerComponent extends PaintableComponent {

    public PlayerComponent(PositionComponent positionComponent) {
	super(positionComponent);
    }

    @Override
    public void paintComponent(org.newdawn.slick.Graphics g) {
	g.setColor(org.newdawn.slick.Color.white);
	int w = GameContext.TILE_WIDTH;
	int h = GameContext.TILE_HEIGHT;
	g.drawLine(0, 0, w, h);
	g.drawLine(0, 1, w - 1, h);
	g.drawLine(w - 1, 0, 0, h);
	g.drawLine(w - 1, 1, 1, h);
    }

    @Override
    public void processMessage(Message message) {
    }
}
