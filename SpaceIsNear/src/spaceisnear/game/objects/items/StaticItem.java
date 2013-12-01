/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects.items;

import spaceisnear.game.GameContext;
import spaceisnear.game.components.PositionComponent;
import spaceisnear.game.objects.GameObject;
import spaceisnear.game.objects.GameObjectType;
import spaceisnear.game.objects.Position;

/**
 *
 * @author white_oak
 */
public abstract class StaticItem extends GameObject {

    private boolean blockable, airBlockable;

    public StaticItem(GameContext context, Position p) {
	super(GameObjectType.ITEM, context);
	PositionComponent pc = new PositionComponent(p, getId());
	addComponents(pc);
    }
}
