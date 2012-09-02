/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import java.awt.Color;
import java.awt.Graphics;
import spaceisnear.game.GameContext;
import spaceisnear.game.GameObject;
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
    public void paintComponent(Graphics g) {
	g.setColor(Color.RED);
	g.drawLine(0, 0, 16, 24);
	g.drawLine(0, 1, 15, 24);
	g.drawLine(15, 0, 0, 24);
	g.drawLine(15, 1, 1, 24);
    }

    @Override
    public void processMessage(Message message) {
    }
}
