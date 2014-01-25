/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.console;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

/**
 *
 * @author White Oak
 */
public class TextField extends AbstractComponent implements MouseListener, KeyListener {

    private StringBuilder text = new StringBuilder();
    private int currentPosition;
    private int x, y;
    private final int width, height;
    private final Font font;
    private Color textColor;

    public TextField(GUIContext container, int x, int y, int width, int height, Font font) {
	super(container);
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
	this.font = font;
    }

    public void addCharacter(char c) {
	if (currentPosition >= text.length()) {
	    text.append(c);
	} else {
	    text.insert(currentPosition, c);
	}
	currentPosition++;
    }

    public void removeCharacter() {
	if (currentPosition > 0) {
	    text.deleteCharAt(currentPosition - 1);
	    currentPosition--;
	}
    }

    @Override
    public void keyPressed(int key, char c) {
	super.keyPressed(key, c);
	if (hasFocus()) {
	    switch (key) {
		case Input.KEY_ENTER:
		    notifyListeners();
		    setFocus(false);
		    break;
		case Input.KEY_BACK:
		    removeCharacter();
		    break;
		case Input.KEY_RIGHT:
		    if (currentPosition > 0) {
			currentPosition--;
		    }
		    break;
		case Input.KEY_LEFT:
		    if (currentPosition < text.length()) {
			currentPosition++;
		    }
		    break;
		default:
//		    if (Character.isLetterOrDigit(c) || Character.isWhitespace(c)) {
//			addCharacter(c);
//		    }
		    if (!Character.isIdentifierIgnorable(c)) {
			addCharacter(c);
		    }
		    break;
	    }
	} else {
	    if (key == Input.KEY_ENTER) {
		setFocus(true);
	    }
	}
    }

    @Override
    public void render(GUIContext container, Graphics g) {
	g.setFont(font);
//	try {
//	    GameConsole.setColor(java.awt.Color.black, (UnicodeFont) g.getFont());
//	} catch (SlickException ex) {
//	    Logger.getLogger(TextField.class.getName()).log(Level.SEVERE, null, ex);
//	}
	g.setColor(textColor);
	g.drawString(text.toString(), x, y);
	if (hasFocus()) {
	    final int spaceWidth = g.getFont().getWidth("w");
	    g.fillRect(x + spaceWidth * currentPosition, y + g.getFont().getLineHeight(), spaceWidth, 2);
	}
    }

    @Override
    public void setLocation(int x, int y) {
	this.x = x;
	this.y = y;
    }

    @Override
    public int getX() {
	return x;
    }

    @Override
    public int getY() {
	return y;
    }

    @Override
    public int getWidth() {
	return width;
    }

    @Override
    public int getHeight() {
	return height;
    }

    public void setTextColor(Color textColor) {
	this.textColor = textColor;
    }

    public String getText() {
	return text.toString();
    }

    public void setText(String string) {
	text = new StringBuilder(string);
	currentPosition = text.length();
    }
}
