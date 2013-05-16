/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects;

import spaceisnear.game.GameContext;
import spaceisnear.game.components.PlayerControllableComponent;

public class GamerPlayer extends Player {

    public GamerPlayer(GameObject parent, GameContext context) {
	super(parent, context);
	addComponents(new PlayerControllableComponent());
    }
}
