/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.server.objects;

import spaceisnear.game.objects.GameObjectType;
import spaceisnear.game.objects.Position;
import spaceisnear.server.GameContext;
import spaceisnear.server.components.PositionComponent;

/**
 *
 * @author white_oak
 */
public class StaticItem extends GameObject{

    public StaticItem(GameObject parent, GameContext context,Position p) {
	super(parent, GameObjectType.ITEM, context);
	PositionComponent pc=new PositionComponent(p);
    }
    
}
