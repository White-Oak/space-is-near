/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.world.game.components;

import java.awt.Color;
import java.awt.Graphics;
import spaceisnear.world.game.GameContext;
import spaceisnear.world.game.GameObject;
import spaceisnear.world.game.messages.Message;

/**
 *
 * @author LPzhelud
 */
public class PlayerComponent extends PaintableComponent {

    public PlayerComponent(GameContext context, GameObject owner, PositionComponent positionComponent) {
	super(context, owner, positionComponent);
    }

    @Override
    public void paintComponent(Graphics g) {
	g.setColor(Color.RED);
	g.drawLine(0, 0, 50, 50);
	g.drawLine(0, 1, 49, 50);
	g.drawLine(50, 0, 0, 50);
	g.drawLine(50, 1, 1, 50);
    }

    @Override
    public void processMessage(Message message) {
    }
}
