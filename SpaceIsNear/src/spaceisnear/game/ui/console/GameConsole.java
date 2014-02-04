package spaceisnear.game.ui.console;

import spaceisnear.game.ui.TextField;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import spaceisnear.Context;
import spaceisnear.game.GameContext;
import spaceisnear.game.messages.MessageLog;
import spaceisnear.game.messages.properties.MessagePropertySet;
import spaceisnear.game.messages.MessageToSend;
import spaceisnear.game.objects.GamerPlayer;

/**
 *
 * @author White Oak
 */
public class GameConsole implements ComponentListener {

    private final int x, y, width, height;
    private final TextField ip;
    private final InGameLog log;
//    Font font = new TrueTypeFont(awtFont, false);
    private final Context context;
    private int scrollBarSize;
    private int scrollBarY;
    private boolean scrollBarClicked;
    @Getter private final UnicodeFont font;
    //
    private final static UnicodeFont[] fonts = new UnicodeFont[10];

    public GameConsole() {
	this.x = 0;
	this.y = 0;
	this.width = 0;
	this.height = 0;
	this.ip = null;
	this.log = null;
	this.context = null;
	this.font = null;
    }

    public GameConsole(int x, int y, int width, int height, GameContainer container, Context context) {
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
	getFontsAtStart();
	font = fonts[0];
	final int lineHeight = font.getLineHeight();
	ip = new TextField(container, x + 10, y + height - lineHeight - 2, width, lineHeight + 4, font);
	log = new InGameLog(font, 30, 2, width - 30, height - 2 - ip.getHeight());
	ip.setTextColor(Color.black);
	ip.addListener(this);
	ip.setFocus(false);
	this.context = context;
	scrollBarSize = sizeOfScrollBar();
    }

    private static void getFontsAtStart() {
	final java.awt.Font awtFont = new java.awt.Font("Monospaced", java.awt.Font.BOLD, 16);
	for (int i = 0; i < fonts.length; i++) {
	    fonts[i] = new UnicodeFont(awtFont);
	    UnicodeFont font = fonts[i];
	    switch (i) {
		case 0:
		    font.getEffects().add(new ColorEffect(java.awt.Color.white));
		    break;
		case 1:
		    font.getEffects().add(new ColorEffect(java.awt.Color.black));
		    break;
		case 2:
		    font.getEffects().add(new ColorEffect(java.awt.Color.lightGray));
		    break;
		case 3:
		    font.getEffects().add(new ColorEffect(java.awt.Color.gray));
		    break;
		case 4:
		    font.getEffects().add(new ColorEffect(java.awt.Color.green));
		    break;
		default:
		    font.getEffects().add(new ColorEffect(java.awt.Color.lightGray));
		    break;
	    }
	    font.addGlyphs(0x0400, 0x04FF);
	    font.addAsciiGlyphs();
	    try {
		font.loadGlyphs();
	    } catch (SlickException ex) {
		Logger.getLogger(GameConsole.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
    }

    public static void setColor(Graphics g, Color c) {
	UnicodeFont unicodeFont = fonts[0];
	if (c == Color.black) {
	    unicodeFont = fonts[1];
	} else if (c == Color.white) {
	    unicodeFont = fonts[0];
	} else if (c == Color.lightGray) {
	    unicodeFont = fonts[2];
	} else if (c == Color.gray) {
	    unicodeFont = fonts[3];
	} else if (c == Color.green) {
	    unicodeFont = fonts[4];
	}
	g.setFont(unicodeFont);
	g.setColor(c);
    }

    public void paint(Graphics g, GameContainer container) {
	final int heightOfTextField = ip.getHeight();
	g.pushTransform();
	g.translate(x, y);
	g.setColor(Color.white);
	g.fillRect(0, 0, width, height);
	//left scrollbar 
	g.setColor(Color.gray);
	g.fillRect(0, 2 + scrollBarY, 20, scrollBarSize);
	g.setColor(Color.white);
	g.fillRect(0, 0, 2, height);
	//bottom input window
	g.setColor(Color.lightGray);
	g.drawLine(0, height - heightOfTextField - 2, width, height - heightOfTextField - 2);
	g.setColor(Color.darkGray);
	g.drawLine(0, height - heightOfTextField - 1, width, height - heightOfTextField - 1);
	g.setColor(Color.lightGray);
	g.drawLine(0, height - heightOfTextField, width, height - heightOfTextField);
	//log
	g.setFont(font);
	log.paint(g, getLineByScrollBarY());
	//
	g.popTransform();
	ip.render(container, g);
    }

    @Override
    public void componentActivated(AbstractComponent source) {
	if (source == ip) {
	    processInputedMessage();
	}
    }

    private void sendMessageFromPlayer(String message) {
	GamerPlayer player = ((GameContext) context).getPlayer();
	String nickname = player.getNickname();
	LogString logString = new LogString(nickname + ": " + message, LogLevel.TALKING, player.getPosition());
	MessageToSend messageToSend = new MessageToSend(new MessageLog(logString));
	context.sendDirectedMessage(messageToSend);
    }

    private void processInputedMessage() {
	String text = ip.getText();
	if (text.startsWith("-")) {
	    String substring = text.substring(1);
	    String[] split = substring.split(" ");
	    switch (split[0]) {
		case "debug":
		    processDebugRequestMessage(split);
		    break;
		case "stoppull":
		    MessagePropertySet messagePropertySet = new MessagePropertySet(((GameContext) context).getPlayerID(), "pull", -1);
		    MessageToSend messageToSend = new MessageToSend(messagePropertySet);
		    context.sendDirectedMessage(messageToSend);
		    break;
		case "h":
		    if (split.length > 2) {
			processBroadcastingMessageFromPlayer(split[1], split);
		    }
		    break;
	    }
	} else {
	    sendMessageFromPlayer(text);
	}
	ip.clear();
    }

    private void processBroadcastingMessageFromPlayer(String frequency, String[] message) {
	if (frequency.equals("all")) {
	    frequency = "145.9";
	}
	if (isGoodFrequency(frequency)) {
	    GamerPlayer player = ((GameContext) context).getPlayer();
	    String nickname = player.getNickname();
	    String standAloneMessage;
	    if (message.length == 3) {
		standAloneMessage = message[2];
	    } else {
		StringBuilder sb = new StringBuilder();
		for (int i = 2; i < message.length; i++) {
		    String string = message[i];
		    sb.append(string);
		    sb.append(' ');
		}
		standAloneMessage = sb.toString();
	    }
	    LogString logString = new LogString(nickname + ": " + standAloneMessage, LogLevel.BROADCASTING, frequency);
	    MessageToSend messageToSend = new MessageToSend(new MessageLog(logString));
	    context.sendDirectedMessage(messageToSend);
	}
    }

    private boolean isGoodFrequency(String frequency) {
	String[] split1 = frequency.split("\\.");
	if (split1.length == 2) {
	    if (split1[0].length() <= 3 && split1[1].length() == 1) {
		String regex = "[0-9]+";
		if (split1[0].matches(regex)) {
		    if (split1[1].length() == 1) {
			return split1[1].matches(regex);
		    }
		}
	    }
	}
	return false;
    }

    private void processDebugRequestMessage(String[] split) {
	if (split.length > 1) {
	    switch (split[1]) {
		case "on":
		    log.setAcceptDebugMessages(true);
		    break;
		case "off":
		    log.setAcceptDebugMessages(false);
		    break;
	    }
	}
    }

    public boolean hasFocus() {
	return ip.hasFocus();
    }

    public void pushMessage(LogString str) {
	log.pushMessage(str);
	scrollBarSize = sizeOfScrollBar();
    }

    public void mouseClicked(int button, int x, int y, int clickCount) {

    }

    public void mousePressed(int button, int x, int y) {
	if (x > this.x && x < this.x + 20) {
	    if (y > scrollBarY && y < scrollBarY + scrollBarSize) {
		scrollBarClicked = true;
	    }
	}
    }

    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
	if (scrollBarClicked) {
	    int move = newy - oldy;
	    processDrag(move);
	}
    }

    private void processDrag(int move) {
	scrollBarY += move;
	if (scrollBarY + scrollBarSize > sizeOfGameLog()) {
	    scrollBarY = sizeOfGameLog() - scrollBarSize;
	} else if (scrollBarY < 0) {
	    scrollBarY = 0;
	}
    }

    public void mouseReleased(int button, int x, int y) {
	scrollBarClicked = false;
    }

    public boolean intersects(int x, int y) {
	boolean xB = x > this.x && x < x + width;
	boolean yB = y > this.y && y < y + height;
	return xB && yB;
    }

    private int linesPerHeight(Font f, int height) {
	return height / f.getLineHeight();
    }

    private int sizeOfScrollBar() {
	float multiplier = ((float) linesPerHeight(font, sizeOfGameLog())) / log.size();
	if (multiplier > 1) {
	    multiplier = 1;
	}
	return (int) (multiplier * sizeOfGameLog());
    }

    private int getLineByScrollBarY() {
	float multiplier = scrollBarY / (float) (sizeOfGameLog() - sizeOfScrollBar());
	int unseenLines = log.getLinesNumber() - linesPerHeight(font, height);
	return (int) (unseenLines * multiplier);
    }

    private int sizeOfGameLog() {
	return height - ip.getHeight() - 4;
    }

    public void mouseWheelMoved(int newValue) {
	processDrag(-newValue >> 2);
    }
}
