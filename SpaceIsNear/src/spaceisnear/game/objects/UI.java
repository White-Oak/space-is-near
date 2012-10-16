/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects;

import spaceisnear.game.GameContext;
import spaceisnear.game.GameObjectTypes;
import spaceisnear.game.components.UIComponent;

public class UI extends GameObject {

    public UI(int id, GameObject parent, GameObjectTypes type, GameContext context) {
	super(id, parent, type, context);
	addComponents(new UIComponent());
    }
}
