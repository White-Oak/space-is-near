/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.ui;

import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

/**
 *
 * @author White Oak
 */
public class ContextMenu {

    private final ContextSubMenu subMenu;

    public ContextMenu(int x, int y, Font font) {
	subMenu = new ContextSubMenu(null, x, y, font);
    }

    public void render(Graphics g) {
	subMenu.render(g);
    }

    public boolean add(ContextMenuItem e) {
	return subMenu.add(e);
    }

    public boolean add(String str) {
	return add(new ContextMenuItem(str));
    }

    public void mouseMoved(int x, int y) {
	subMenu.mouseMoved(x, y);
    }

    public void mouseClicked(int button, int x, int y, int clickCount) {
	subMenu.mouseClicked(button, x, y, clickCount);
    }
}
