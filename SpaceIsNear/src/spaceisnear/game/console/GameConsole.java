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
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;

/**
 *
 * @author White Oak
 */
public class GameConsole implements ComponentListener {

    private final int x, y, width, height;
    private final TextField ip;
    private final InGameLog log = new InGameLog();
    java.awt.Font awtFont = new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 20);
    UnicodeFont font = new UnicodeFont(awtFont);
//    Font font = new TrueTypeFont(awtFont, false);

    public GameConsole(int x, int y, int width, int height, GameContainer container) {
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
	font.getEffects().add(new ColorEffect(java.awt.Color.black));
	font.addGlyphs("йцукенгшщзхъфывапролджэячсмитьбю");
	font.addNeheGlyphs();
	try {
	    font.loadGlyphs();
	} catch (SlickException ex) {
	    Logger.getLogger(GameConsole.class.getName()).log(Level.SEVERE, null, ex);
	}
	final int lineHeight = font.getLineHeight();
	ip = new TextField(container, x + 10, y + height - lineHeight - 2, width, lineHeight + 4, font);
	ip.setTextColor(Color.black);
	ip.addListener(this);
	ip.setFocus(false);
    }

    public void paint(Graphics g, GameContainer container) {
	final int heightOfTextField = ip.getHeight();
	g.pushTransform();
	g.translate(x, y);
	g.setColor(Color.white);
	g.fillRect(0, 0, width, height);
	//left scrollbar 
	g.setColor(Color.gray);
	g.fillRect(0, 2, 20, height - heightOfTextField - 4);
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
	log.paint(g, 0, 0);
	//
	g.popTransform();
	ip.render(container, g);
    }

    @Override
    public void componentActivated(AbstractComponent source) {
	if (source == ip) {
	    log.pushMessage(ip.getText(), LogLevel.TALKING);
	    ip.setText("");
	    ip.setFocus(false);
	}
    }

    public boolean hasFocus() {
	return ip.hasFocus();
    }

}
