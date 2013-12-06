/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import org.newdawn.slick.Graphics;
import spaceisnear.game.messages.Message;

public class ItemPaintableComponent extends PaintableComponent {

    @Override
    public void paintComponent(Graphics g) {
    }

    @Override
    public void processMessage(Message message) {
    }

    public ItemPaintableComponent() {
	super(ComponentType.ITEM_PAINTABLE);
    }

}
