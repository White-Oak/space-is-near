/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects;

import spaceisnear.game.GameContext;
import spaceisnear.game.components.NetworkControllableComponent;

/**
 *
 * @author LPzhelud
 */
public class TheOtherPlayer extends Player {

    public TheOtherPlayer(GameObject parent, GameContext context) {
	super(parent, context);
	addComponents(new NetworkControllableComponent());
    }
}
