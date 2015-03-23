/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.ui.console;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import spaceisnear.game.GameContext;
import spaceisnear.game.ui.UIElement;

/**
 *
 * @author White Oak
 */
@RequiredArgsConstructor public class InGameLog {

    private final Stack<ChatString> stack = new Stack<>();
    private final int x, y, width, height;
    private int linesNumber = 0;
    private final BitmapFont font = UIElement.font;
    @Setter private boolean acceptDebugMessages;

    public void paint(Batch batch, int startingLine) {
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
	    System.out.println(str.getReceiverID());
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
		return BROADCASTING_COLOR;
	    case WARNING:
		return WARNING_COLOR;
	    case OOC:
		return OOC_COLOR;
	    case PRIVATE:
		return PRIVATE_COLOR;
	    case WHISPERING:
		return WHISPERING_COLOR;
	    default:
		return Color.GRAY;
	}
    }
    private static final Color WHISPERING_COLOR = new Color(0x7f8c8dff);
    private static final Color PRIVATE_COLOR = new Color(0x9b59b6ff);
    private static final Color OOC_COLOR = new Color(0x3498dbff);
    private static final Color WARNING_COLOR = new Color(0xe74c3cff);
    private static final Color BROADCASTING_COLOR = new Color(0x16d065ff);

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
	    if (line.charAt(i) == '\n') {
		String toAdd = line.substring(0, i);
		list.add(toAdd);
		line = line.substring(i + 1);
		previousSpace = 0;
		i = 0;
		continue;
	    }
	    if (font.getBounds(before).width > maxWidth) {
		if (previousSpace != 0) {
		    String toAdd = line.substring(0, previousSpace);
		    list.add(toAdd);
		    line = line.substring(previousSpace + 1);
		    previousSpace = 0;
		    i = 0;
		    continue;
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
