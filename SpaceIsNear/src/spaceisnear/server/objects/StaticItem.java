/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.server.objects;

import spaceisnear.game.components.PositionComponent;
import spaceisnear.game.objects.GameObjectType;
import spaceisnear.game.objects.Position;
import spaceisnear.server.GameContext;

/**
 *
 * @author white_oak
 */
public class StaticItem extends GameObject {

    public StaticItem(GameContext context, Position p) {
	super(GameObjectType.ITEM, context);
	PositionComponent pc = new PositionComponent(p, getId());
    }

}
