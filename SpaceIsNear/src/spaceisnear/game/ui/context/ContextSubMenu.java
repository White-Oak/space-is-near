/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.ui.context;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Setter;
import org.newdawn.slick.*;
import spaceisnear.game.ui.console.GameConsole;

/**
 *
 * @author White Oak
 */
public class ContextSubMenu extends ContextMenuItem {

    private final List<ContextMenuItem> items = new ArrayList<>();
    @Setter(AccessLevel.PACKAGE) private int y;
    @Setter(AccessLevel.PACKAGE) private int x;
    private int maxWidth;
    private int height;
    @Setter(AccessLevel.PACKAGE) private Font font;
    private int selected;
    @Setter private ActionListener actionListener;

    public ContextSubMenu(String label) {
	super(label);
    }

    ContextSubMenu(String label, int x, int y, Font font) {
	super(label);
	this.x = x;
	this.y = y;
	this.font = font;
    }

    public void render(Graphics g) {
	g.pushTransform();
	g.translate(x, y);
	g.setColor(Color.white);
	g.fillRect(0, 0, maxWidth, height);
	g.setColor(Color.black);
	g.drawRect(-1, -1, maxWidth + 1, height + 1);
	g.fillRect(0, selected * font.getLineHeight(), maxWidth, font.getLineHeight());
	for (int i = 0; i < items.size(); i++) {
	    ContextMenuItem contextMenuItem = items.get(i);
	    final int currentPosition = i * font.getLineHeight();
	    if (i == selected) {
		g.setColor(Color.white);
		if (contextMenuItem instanceof ContextSubMenu) {
		    g.fillRect(maxWidth - 10, currentPosition + (font.getLineHeight() >> 1) - 1, 3, 3);
		    ContextSubMenu subMenu = (ContextSubMenu) contextMenuItem;
		    g.popTransform();
		    subMenu.render(g);
		    g.pushTransform();
		    g.translate(x, y);
		}
		GameConsole.setColor(g, Color.white);
	    } else {
		g.setColor(Color.black);
		if (contextMenuItem instanceof ContextSubMenu) {
		    g.fillRect(maxWidth - 10, currentPosition + (font.getLineHeight() >> 1) - 1, 3, 3);
		}
		GameConsole.setColor(g, Color.black);
	    }
	    g.drawString(contextMenuItem.getLabel(), 0, currentPosition);
	}
	g.popTransform();
    }

    public boolean add(String str) {
	return add(new ContextMenuItem(str));
    }

    public boolean add(ContextMenuItem e) {
	if (font == null) {
	    throw new RuntimeException("Sub menu should be added to Context menu before items could be added to it.");
	}
	int width = font.getWidth(e.getLabel());
	if (width > maxWidth) {
	    maxWidth = width + 20;
	    for (ContextMenuItem contextMenuItem : items) {
		if (contextMenuItem instanceof ContextSubMenu) {
		    ContextSubMenu subMenu = (ContextSubMenu) contextMenuItem;
		    subMenu.x = x + maxWidth + 1;
		}
	    }
	}
	height += font.getLineHeight();
	if (e instanceof ContextSubMenu) {
	    ContextSubMenu subMenu = (ContextSubMenu) e;
	    subMenu.setFont(font);
	    subMenu.setX(x + maxWidth + 1);
	    subMenu.setY(y + items.size() * font.getLineHeight());
	}
	return items.add(e);
    }

    public boolean intersects(int x, int y) {
	boolean xB = x > this.x && x < this.x + maxWidth;
	boolean yB = y > this.y && y < this.y + height;
	return xB && yB;
    }

    public void mouseMoved(int x, int y) {
	if (intersects(x, y)) {
	    x -= this.x;
	    y -= this.y;
	    selected = y / font.getLineHeight();
	} else {
	    for (ContextMenuItem contextMenuItem : items) {
		if (contextMenuItem instanceof ContextSubMenu) {
		    ContextSubMenu subMenu = (ContextSubMenu) contextMenuItem;
		    subMenu.mouseMoved(x, y);
		}
	    }
	}
    }

    public void mouseClicked(int button, int x, int y, int clickCount) {
	if (intersects(x, y)) {
	    if (actionListener != null) {
		x -= this.x;
		y -= this.y;
		int selected = y / font.getLineHeight();
		actionListener.itemActivated(items.get(selected));
	    }
	} else {
	    for (ContextMenuItem contextMenuItem : items) {
		if (contextMenuItem instanceof ContextSubMenu) {
		    ContextSubMenu subMenu = (ContextSubMenu) contextMenuItem;
		    subMenu.mouseClicked(button, x, y, clickCount);
		}
	    }
	}
    }
}
