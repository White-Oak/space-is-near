/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import lombok.*;

public class MessageMoved extends Message {

    @Getter private final int x, y;

    public MessageMoved(int x, int y) {
	super(MessageTypes.MOVED);
	this.x = x;
	this.y = y;
    }
}
