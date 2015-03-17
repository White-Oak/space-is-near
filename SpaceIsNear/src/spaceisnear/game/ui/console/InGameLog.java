/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.ui.console;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.*;
import lombok.Setter;
import spaceisnear.game.GameContext;

/**
 *
 * @author White Oak
 */
public class InGameLog {

    private final Stack<LogString> stack = new Stack<>();
    private final int x, y, width, height;
    private int linesNumber = 0;
    private final BitmapFont font = new BitmapFont(Gdx.files.classpath("default.fnt"), true);
    @Setter private boolean acceptDebugMessages;

    public InGameLog(int x, int y, int width, int height) {
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
    }

    public void paint(SpriteBatch batch, int startingLine) {
	final int startingY = -startingLine * (int) font.getLineHeight();
	for (int i = 0, linesGone = 0, linesDrawn = 0; i < stack.size(); i++) {
	    LogString get = stack.get(i);
	    font.setColor(getColorOfLevel(get));
	    String[] strings = splitByLines(get.toString(), width, font);
	    for (int j = 0; j < strings.length && linesDrawn < linesPerHeight(font, height); j++, linesGone++) {
		String string = strings[j];
		final int ycoord = y + ((int) font.getLineHeight()) * linesGone + startingY;
		if (ycoord > 0) {
		    font.draw(batch, string, x, ycoord);
		    linesDrawn++;
		}
	    }
	}
    }

    private int linesPerHeight(BitmapFont f, int height) {
	return height / (int) f.getLineHeight();
    }

    public void pushMessage(LogString str, GameContext context) {
	if (str.getReceiverID() != 0 && str.getReceiverID() != context.getPlayerID()) {
	    return;
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
		return Color.GRAY;
	    case TALKING:
		return Color.BLACK;
	    case BROADCASTING:
		return new Color(0, 0.5f, 0, 1);
	    case WARNING:
		return new Color(0xe74c3cff);
	    case OOC:
		return Color.BLUE;
	    case PRIVATE:
		return Color.MAGENTA;
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

    private static String[] splitByLines(String line, int width, BitmapFont font) {
	List<String> strings = wrapLineInto(line, font, width);
	return strings.toArray(new String[strings.size()]);
    }

    /**
     * Given a line of text and font metrics information, wrap the line and add the new line(s) to <var>list</var>.
     *
     * @param line a line of text
     * @param list an output list of strings
     * @param fm font metrics
     * @param maxWidth maximum width of the line(s)
     */
    private static List<String> wrapLineInto(String line, BitmapFont font, int maxWidth) {
	int len = line.length();
	List<String> list = new LinkedList<>();
	int width;
	while (len > 0 && (width = (int) font.getBounds(line).width) > maxWidth) {
	    // Guess where to split the line. Look for the next space before
	    // or after the guess.
	    int guess = len * maxWidth / width;
	    String before = line.substring(0, guess).trim();

	    width = (int) font.getBounds(before).width;
	    int pos;
	    if (width > maxWidth) // Too long
	    {
		pos = findBreakBefore(line, guess);
	    } else { // Too short or possibly just right
		pos = findBreakAfter(line, guess);
		if (pos != -1) { // Make sure this doesn't make us too long
		    before = line.substring(0, pos).trim();
		    if ((int) font.getBounds(before).width > maxWidth) {
			pos = findBreakBefore(line, guess);
		    }
		}
	    }
	    if (pos == -1) {
		pos = guess; // Split in the middle of the word
	    }
	    list.add(line.substring(0, pos).trim());
	    line = line.substring(pos).trim();
	    len = line.length();
	}
	if (len > 0) {
	    list.add(line);
	}
	return list;
    }

    /**
     * Returns the index of the first whitespace character or '-' in <var>line</var>
     * that is at or before <var>start</var>. Returns -1 if no such character is found.
     *
     * @param line a string
     * @param start where to star looking
     */
    private static int findBreakBefore(String line, int start) {
	for (int i = start; i >= 0; --i) {
	    char c = line.charAt(i);
	    if (Character.isWhitespace(c) || c == '-') {
		return i;
	    }
	}
	return -1;
    }

    /**
     * Returns the index of the first whitespace character or '-' in <var>line</var>
     * that is at or after <var>start</var>. Returns -1 if no such character is found.
     *
     * @param line a string
     * @param start where to star looking
     */
    private static int findBreakAfter(String line, int start) {
	int len = line.length();
	for (int i = start; i < len; ++i) {
	    char c = line.charAt(i);
	    if (Character.isWhitespace(c) || c == '-') {
		return i;
	    }
	}
	return -1;
    }
}
