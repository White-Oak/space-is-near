/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.server.objects;

import spaceisnear.server.GameContext;
import spaceisnear.server.components.PlayerControllableComponent;

public class GamerPlayer extends Player {

    public GamerPlayer(GameObject parent, GameContext context) {
	super(parent, context);
	addComponents(new PlayerControllableComponent());
    }
}
