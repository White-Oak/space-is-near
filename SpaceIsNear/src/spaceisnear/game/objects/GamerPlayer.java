/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects;

import spaceisnear.game.GameContext;
import spaceisnear.game.bundles.ObjectBundle;
import spaceisnear.game.components.PlayerControllableComponent;

public class GamerPlayer extends Player {

    public GamerPlayer(GameContext context) {
	super(context);
	addComponents(new PlayerControllableComponent(this));
    }

    public static Player getInstance(ObjectBundle bundle, GameContext context) {
	Player player = new Player(context);
	player.setId(bundle.getObjectID());
	player.addComponents(bundle.getState().getComponents());
	return player;
    }
}
