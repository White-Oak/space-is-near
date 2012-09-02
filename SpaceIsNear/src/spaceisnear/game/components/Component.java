/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import lombok.*;
import spaceisnear.game.GameContext;
import spaceisnear.game.GameObject;
import spaceisnear.game.messages.Message;

/**
 *
 * @author LPzhelud
 */
public abstract class Component {

    @Getter private ComponentState state;
    private GameContext context = null;
    @Getter private GameObject owner;

    public abstract void processMessage(Message message);

    public void setContext(GameContext context) {
	if (this.context == null) {
	    this.context = context;
	}
    }

    public void setOwner(GameObject owner) {
	if (this.owner == null) {
	    this.owner = owner;
	}
    }
}
