/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.server.objects;

import spaceisnear.game.components.PlayerControllableComponent;

/**
 *
 * @author White Oak
 */
public class GamerPlayer extends spaceisnear.game.objects.Player {

    public GamerPlayer() {
	super(null);
	addComponents(new PlayerControllableComponent(this));
    }
}
