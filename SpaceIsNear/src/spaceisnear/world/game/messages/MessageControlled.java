/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.world.game.messages;

import lombok.Getter;

/**
 *
 * @author LPzhelud
 */
public class MessageControlled extends Message {

    @Getter private final Type type;

    public MessageControlled(Type type) {
	super(MessageTypes.CONTROLLED);
	this.type = type;
    }

    public enum Type {

	UP, DOWN, LEFT, RIGHT
    }
}
