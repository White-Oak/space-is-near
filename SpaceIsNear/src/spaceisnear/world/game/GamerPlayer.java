/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.world.game;

import spaceisnear.world.game.components.PlayerControllableComponent;

public class GamerPlayer extends Player {

    public GamerPlayer(int id, GameObject parent, GameContext context) {
	super(id, parent, context);
	addComponents(new PlayerControllableComponent());
    }
}
