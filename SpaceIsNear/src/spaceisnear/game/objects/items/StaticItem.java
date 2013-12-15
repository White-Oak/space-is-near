/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects.items;

import spaceisnear.game.GameContext;
import spaceisnear.game.components.ItemPaintableComponent;
import spaceisnear.game.components.ItemPropertiesComponent;
import spaceisnear.game.components.PositionComponent;
import spaceisnear.game.objects.ClientGameObject;
import spaceisnear.game.objects.GameObjectType;
import spaceisnear.game.objects.Position;

/**
 *
 * @author white_oak
 */
public abstract class StaticItem extends ClientGameObject {

    public StaticItem(GameContext context, Position p, int itemId) {
	super(GameObjectType.ITEM, context);
	PositionComponent pc = new PositionComponent(p);
	addComponents(pc, new ItemPropertiesComponent(itemId), new ItemPaintableComponent());
    }
}
