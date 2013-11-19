/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects;

import spaceisnear.game.GameContext;
import spaceisnear.game.components.UIComponent;

public class UI extends GameObject {

    public UI(GameObjectType type, GameContext context) {
	super(type, context);
	addComponents(new UIComponent());
    }
}
