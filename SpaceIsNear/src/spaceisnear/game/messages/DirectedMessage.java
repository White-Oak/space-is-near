/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import lombok.Getter;

/**
 *
 * @author White Oak
 */
public abstract class DirectedMessage extends Message {

    @Getter protected final int id;

    public DirectedMessage(MessageType messageType, int id) {
	super(messageType);
	this.id = id;
    }

}
