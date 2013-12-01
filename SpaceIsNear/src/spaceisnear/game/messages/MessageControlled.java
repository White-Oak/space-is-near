// Generated by delombok at Sun Dec 01 13:28:22 MSK 2013
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import spaceisnear.game.bundles.MessageBundle;

/**
 * @author LPzhelud
 */
public class MessageControlled extends DirectedMessage implements NetworkableMessage {

    private final Type type;

    public MessageControlled(Type type) {
	super(MessageType.CONTROLLED);
	this.type = type;
    }

    public enum Type {

	UP,
	DOWN,
	LEFT,
	RIGHT;

    }

    @Override
    public MessageBundle getBundle() {
	return null;
    }

    public Type getType() {
	return this.type;
    }
}
