/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.console;

import java.util.ArrayList;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;

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
//	    try {
//		GameConsole.setColor(getColorOfLevel(get), (UnicodeFont) g.getFont());
//	    } catch (SlickException ex) {
//		Logger.getLogger(InGameLog.class.getName()).log(Level.SEVERE, null, ex);
//	    }
	    g.setColor(getColorOfLevel(get));
	    String[] strings = splitByLines(get.toString(), width, g.getFont());
	    for (int j = startingMessage == i ? startingLine : 0; j < strings.length; j++, linesDrawn++) {
		String string = strings[j];
		g.drawString(string, x, y + g.getFont().getLineHeight() * linesDrawn);
	    }
	}
    }

    public void pushMessage(LogString str) {
	if (!stack.empty() && str.getMessage().equals(pullActualMessage())) {
	    increaseTimesOfLastMessage();
	} else {
	    stack.push(str);
	}
    }

    private String pullActualMessage() {
	return stack.empty() ? null : stack.peek().getMessage();
    }

    private void increaseTimesOfLastMessage() {
	stack.peek().increaseTimes();
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
		    previousCut = previousSpace;
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
