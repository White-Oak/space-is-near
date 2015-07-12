/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.ui.console;

import java.io.Serializable;
import lombok.Getter;
import spaceisnear.game.ui.Position;

/**
 *
 * @author White Oak
 */
public class ChatString implements Serializable {

    @Getter private final String message;
    private int timesMessaged = 1;
    @Getter private final LogLevel level;
    @Getter private final Position position;
    @Getter private final String frequency;
    @Getter private final int receiverID;

    ChatString() {
	this.message = null;
	this.level = null;
	this.position = null;
	this.frequency = null;
	this.receiverID = 0;
    }

    public ChatString(String message, LogLevel level) {
	this(message, level, null, null, 0);
    }

    private ChatString(String message, LogLevel level, Position position, String frequency, int receiverID) {
	this.message = message;
	this.level = level;
	this.position = position;
	this.frequency = frequency;
	this.receiverID = receiverID;
    }

    public ChatString(String message, LogLevel level, Position position) {
	this(message, level, position, null, 0);
    }

    public ChatString(String message, LogLevel level, String frequency) {
	this(message, level, null, frequency, 0);
    }

    public ChatString(String message, LogLevel level, int receiverID) {
	this(message, level, null, null, receiverID);
    }

    @Override
    public String toString() {
	String messageToReturn = this.message;
	if (frequency != null) {
	    messageToReturn = "[" + frequency + "]: " + messageToReturn;
	}
	if (timesMessaged == 1) {
	    return messageToReturn;
	} else {
	    return messageToReturn + ": x" + timesMessaged;
	}
    }

    public void increaseTimes() {
	timesMessaged++;
    }
}
