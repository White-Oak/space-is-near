/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.console;

import java.util.ArrayList;
import java.util.Stack;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

/**
 *
 * @author White Oak
 */
public class InGameLog {

    private final Stack<LogString> stack = new Stack<>();
    private int x = 30, y = 2, width = 370, height = 700;

    public void paint(Graphics g, int startingMessage, int startingLine) {
	for (int i = startingMessage, linesDrawn = 0; i < stack.size(); i++) {
	    LogString get = stack.get(i);
	    g.setColor(getColorOfLevel(get));
	    String[] strings = splitByLines(get.toString(), width, g.getFont());
	    for (int j = startingMessage == i ? startingLine : 0; j < strings.length; j++, linesDrawn++) {
		String string = strings[j];
		g.drawString(string, x, y + g.getFont().getLineHeight() * linesDrawn);
	    }
	}
    }

    public void pushMessage(String msg, LogLevel level) {
	if (!stack.empty() && msg.equals(pullActualMessage())) {
	    increaseTimesOfLastMessage();
	} else {
	    stack.push(new LogString(msg, level));
	}
    }

    private String pullActualMessage() {
	return stack.empty() ? null : stack.peek().getMessage();
    }

    private void increaseTimesOfLastMessage() {
	stack.peek().increaseTimes();
    }

    private class LogString {

	private final String message;
	private int timesMessaged = 1;
	private final LogLevel level;

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

    private static Color getColorOfLevel(LogString str) {
	LogLevel level = str.level;
	switch (level) {
	    case DEBUG:
		return Color.lightGray;
	    case TALKING:
		return Color.black;
	    default:
		return Color.gray;
	}
    }

    private static String[] splitByLines(String line, int width, Font font) {
	ArrayList<String> strings = new ArrayList<>();
	int previousCut = 0, previousSpace = 0;
	while (true) {
	    int indexOf = line.indexOf(' ', previousSpace);
	    if (indexOf == -1) {
		strings.add(line.substring(previousCut));
		break;
	    }
	    if (font.getWidth(line.substring(previousCut, indexOf)) > width) {
		if (previousCut != previousSpace) {
		    strings.add(line.substring(previousCut, previousSpace));
		    previousCut = indexOf + 1;
		} else {
		    strings.add(line.substring(previousCut, indexOf));
		    previousCut = indexOf + 1;
		}
	    }
	    previousSpace = indexOf + 1;
	}
	return strings.toArray(new String[strings.size()]);
    }
}
