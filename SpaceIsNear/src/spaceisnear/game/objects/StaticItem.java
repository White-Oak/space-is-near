/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects;

import spaceisnear.game.GameContext;
import spaceisnear.game.components.PositionComponent;

/**
 *
 * @author white_oak
 */
public class StaticItem extends GameObject {

    public StaticItem(GameContext context, Position p) {
	super(GameObjectType.ITEM, context);
	PositionComponent pc = new PositionComponent(p);
	addComponents(pc);
    }
}
