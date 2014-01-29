/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.console;

import spaceisnear.game.objects.Position;

/**
 *
 * @author White Oak
 */
public class LogString {

    private final String message;
    private int timesMessaged = 1;
    private final LogLevel level;
    private final Position position;
    private final String frequency;

    public LogString(String message, LogLevel level) {
	this(message, level, null, null);
    }

    public LogString(String message, LogLevel level, Position position, String frequency) {
	this.message = message;
	this.level = level;
	this.position = position;
	this.frequency = frequency;
    }

    public String getMessage() {
	return message;
    }

    @Override
    public String toString() {
	String message = this.message;
	if (frequency != null) {
	    message = "[" + frequency + "]: " + message;
	}
	if (timesMessaged == 1) {
	    return message;
	} else {
	    return message + ": x" + timesMessaged;
	}
    }

    public void increaseTimes() {
	timesMessaged++;
    }

    public LogLevel getLevel() {
	return level;
    }

    public Position getPosition() {
	return position;
    }

    public String getFrequency() {
	return frequency;
    }
}
