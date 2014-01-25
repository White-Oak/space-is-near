/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.console;

/**
 *
 * @author White Oak
 */
public class LogString {

    private final String message;
    private int timesMessaged = 1;
    final LogLevel level;

    public LogString(String message, LogLevel level) {
	this.message = message;
	this.level = level;
    }

    public String getMessage() {
	return message;
    }

    @Override
    public String toString() {
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

}
