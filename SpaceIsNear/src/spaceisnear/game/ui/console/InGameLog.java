/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.ui.console;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.Stack;
import lombok.Setter;

/**
 *
 * @author White Oak
 */
public class InGameLog {

    private final Stack<LogString> stack = new Stack<>();
    private final int x, y, width, height;
    private int linesNumber = 0;
    private final BitmapFont font;
    @Setter private boolean acceptDebugMessages;

    public InGameLog(BitmapFont font, int x, int y, int width, int height) {
	this.font = font;
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
    }
    private final SpriteBatch batch = new SpriteBatch();

    public void paint(int startingLine) {
	final int startingY = -startingLine * (int) font.getLineHeight();
	batch.begin();
	for (int i = 0, linesGone = 0, linesDrawn = 0; i < stack.size(); i++) {
	    LogString get = stack.get(i);
	    batch.setColor(getColorOfLevel(get));
//	    String[] strings = splitByLines(get.toString(), width, g.getFont());
	    String[] strings = new String[]{get.toString()};
	    for (int j = 0; j < strings.length && linesDrawn < linesPerHeight(font, height); j++, linesGone++) {
		String string = strings[j];
		final int ycoord = y + ((int) font.getLineHeight()) * linesGone + startingY;
		if (ycoord > 0) {
		    font.draw(batch, string, x, ycoord);;
		    linesDrawn++;
		}
	    }
	}
	batch.end();
    }

    private int linesPerHeight(BitmapFont f, int height) {
	return height / (int) f.getLineHeight();
    }

    public void pushMessage(LogString str) {
	if (str.getLevel() == LogLevel.BROADCASTING) {
	    System.out.println("oh lol");
	}
	if (!acceptDebugMessages && str.getLevel() == LogLevel.DEBUG) {
	    return;
	}
	if (!stack.empty() && str.getMessage().equals(pullActualMessage())) {
	    increaseTimesOfLastMessage();
	} else {
	    stack.push(str);
	    addLastMessageToLinesNumber();
	}
    }

    private String pullActualMessage() {
	return stack.empty() ? null : stack.peek().getMessage();
    }

    private void increaseTimesOfLastMessage() {
	stack.peek().increaseTimes();
    }

    private static Color getColorOfLevel(LogString str) {
	LogLevel level = str.getLevel();
	switch (level) {
	    case DEBUG:
		return Color.LIGHT_GRAY;
	    case TALKING:
		return Color.BLACK;
	    case BROADCASTING:
		return Color.GREEN;
	    default:
		return Color.GRAY;
	}
    }

    public synchronized int size() {
	return stack.size();
    }

    public int getLinesNumber() {
	return linesNumber;
    }

    private void addLastMessageToLinesNumber() {
	//TODO Notice that when last message will be modified, it may be bad
//	linesNumber += splitByLines(pullActualMessage(), width, font).length;
	linesNumber++;
    }

//    private static String[] splitByLines(String line, int width, Font font) {
//	ArrayList<String> strings = new ArrayList<>();
//	int previousCut = 0, previousSpace = 0;
//	while (true) {
//	    int indexOf = line.indexOf(' ', previousSpace);
//	    if (indexOf == -1) {
//		strings.add(line.substring(previousCut));
//		break;
//	    }
//	    if (font.getWidth(line.substring(previousCut, indexOf)) > width) {
//		if (previousCut != previousSpace) {
//		    strings.add(line.substring(previousCut, previousSpace));
//		    previousCut = previousSpace;
//		} else {
//		    strings.add(line.substring(previousCut, indexOf));
//		    previousCut = indexOf + 1;
//		}
//	    }
//	    previousSpace = indexOf + 1;
//	}
//	return strings.toArray(new String[strings.size()]);
//    }
}
