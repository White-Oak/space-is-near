/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.console;

import java.util.logging.Level;
import java.util.logging.Logger;
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
    java.awt.Font awtFont = new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 15);
    UnicodeFont font = new UnicodeFont(awtFont);
//    Font font = new TrueTypeFont(awtFont, false);
    private final Context context;
    private int scrollBarSize;
    private int scrollBarY;
    private boolean scrollBarClicked;

    public GameConsole(int x, int y, int width, int height, GameContainer container, Context context) {
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
	font.getEffects().add(new ColorEffect(java.awt.Color.black));
	font.getEffects().add(new ColorEffect(java.awt.Color.lightGray));
	font.getEffects().add(new ColorEffect(java.awt.Color.gray));
//	font.addGlyphs("йцукенгшщзхъфывапролджэячсмитьбю");
	font.addGlyphs(0x0400, 0x04FF);
	font.addAsciiGlyphs();
	try {
	    font.loadGlyphs();
	} catch (SlickException ex) {
	    Logger.getLogger(GameConsole.class.getName()).log(Level.SEVERE, null, ex);
	}
	final int lineHeight = font.getLineHeight();
	ip = new TextField(container, x + 10, y + height - lineHeight - 2, width, lineHeight + 4, font);
	log = new InGameLog(font, 30, 2, width - 30, height - 2 - ip.getHeight());
	ip.setTextColor(Color.black);
	ip.addListener(this);
	ip.setFocus(false);
	this.context = context;
	scrollBarSize = sizeOfScrollBar();
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
		    break;
	    }
	} else {
	    sendMessageFromPlayer(text);
	}
	ip.clear();
    }

    public boolean hasFocus() {
	return ip.hasFocus();
    }

    public void pushMessage(LogString str) {
	log.pushMessage(str);
	scrollBarSize = sizeOfScrollBar();
    }

    public static void setColor(java.awt.Color color, UnicodeFont font) throws SlickException {
	font.getEffects().clear();
	font.getEffects().add(new ColorEffect(color));
	font.clearGlyphs();
	font.addGlyphs(0x0400, 0x04FF);
	font.addAsciiGlyphs();
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
