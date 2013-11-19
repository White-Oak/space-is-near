/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import java.util.ArrayList;
import lombok.*;
import spaceisnear.game.GameContext;
import spaceisnear.game.objects.GameObject;
import spaceisnear.game.messages.Message;

/**
 *
 * @author LPzhelud
 */
public abstract class Component {

    @Getter private ArrayList<ComponentState> states = new ArrayList<>();
    @Getter(AccessLevel.PROTECTED) private GameContext context = null;

    public abstract void processMessage(Message message);

    public void setContext(GameContext context) {
	if (this.context == null) {
	    this.context = context;
	}
    }
}
