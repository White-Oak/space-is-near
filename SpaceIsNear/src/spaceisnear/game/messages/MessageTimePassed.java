/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import lombok.*;

public class MessageTimePassed extends Message {

    @Getter private int timePassed;

    public MessageTimePassed(int time) {
	super(MessageType.TIME_PASSED);
	this.timePassed = time;
    }
}
