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
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import spaceisnear.game.GameContext;

/**
 *
 * @author White Oak
 */
@RequiredArgsConstructor public class InGameLog {

    private final Stack<ChatString> stack = new Stack<>();
    private final int x, y, width, height;
    private int linesNumber = 0;
    private final BitmapFont font = new BitmapFont(Gdx.files.classpath("segoe_ui.fnt"), true);
    @Setter private boolean acceptDebugMessages;

    public void paint(SpriteBatch batch, int startingLine) {
	final int startingY = -startingLine * (int) font.getLineHeight();
	for (int i = 0, linesGone = 0, linesDrawn = 0; i < stack.size(); i++) {
	    ChatString get = stack.get(i);
	    font.setColor(getColorOfLevel(get));
	    String[] strings = splitByLines(get.toString(), width, font);
	    for (int j = 0; j < strings.length && linesDrawn < getMaxLinesPerHeight(font, height); j++, linesGone++) {
		String string = strings[j];
		final int ycoord = y + ((int) font.getLineHeight()) * linesGone + startingY;
		if (ycoord > 0) {
		    font.draw(batch, string, x, ycoord);
		    linesDrawn++;
		}
	    }
	}
    }

    private int getMaxLinesPerHeight(BitmapFont f, int height) {
	return height / (int) f.getLineHeight();
    }

    public void pushMessage(ChatString str, GameContext context) {
	if (str.getReceiverID() != 0 && str.getReceiverID() != context.getPlayerID()) {
	    return;
	}
	if (!acceptDebugMessages && str.getLevel() == LogLevel.DEBUG) {
	    return;
	}
	if (!stack.empty() && str.getMessage().equals(getActualMessage())) {
	    increaseTimesOfLastMessage();
	} else {
	    stack.push(str);
	    addLastMessageToLinesNumber();
	}
    }

    private String getActualMessage() {
	return stack.empty() ? null : stack.peek().getMessage();
    }

    private void increaseTimesOfLastMessage() {
	stack.peek().increaseTimes();
    }

    private static Color getColorOfLevel(ChatString str) {
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
	linesNumber += splitByLines(getActualMessage(), width, font).length;
    }

    private String[] splitByLines(String line, int width, BitmapFont font) {
	List<String> strings = breakIntoLines(line, font, width);
	return strings.toArray(new String[strings.size()]);
    }

    private List<String> breakIntoLines(String line, BitmapFont font, int maxWidth) {
	List<String> list = new LinkedList<>();
	int i = 0;
	int previousSpace = 0;
	while (i < line.length() && (int) font.getBounds(line).width > maxWidth) {
	    String before = line.substring(0, i);
	    if (line.charAt(i) == ' ') {
		previousSpace = i;
	    }
	    if (font.getBounds(before).width > maxWidth) {
		if (previousSpace != 0) {
		    String toAdd = line.substring(0, previousSpace);
		    list.add(toAdd);
		    line = line.substring(previousSpace + 1);
		    previousSpace = 0;
		    i = 0;
		}
	    }
	    i++;
	}
	if (!line.isEmpty()) {
	    list.add(line);
	}
	return list;
    }
}
