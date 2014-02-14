/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.ui.context;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Setter;

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
    @Setter(AccessLevel.PACKAGE) private BitmapFont font;
    private int selected;
    @Setter private ActionListener actionListener;

    public ContextSubMenu(String label) {
	super(label);
    }

    ContextSubMenu(String label, int x, int y, BitmapFont font) {
	super(label);
	this.x = x;
	this.y = y;
	this.font = font;
    }
    private final ShapeRenderer renderer = new ShapeRenderer();

    public void render(SpriteBatch batch) {
	renderer.translate(x, y, 0);
	renderer.begin(ShapeRenderer.ShapeType.FilledRectangle);
	renderer.setColor(Color.WHITE);
	renderer.filledRect(0, 0, maxWidth, height);
	renderer.setColor(Color.BLACK);
	renderer.filledRect(0, selected * font.getLineHeight(), maxWidth, font.getLineHeight());
	renderer.begin(ShapeRenderer.ShapeType.Rectangle);
	renderer.rect(-1, -1, maxWidth + 1, height + 1);
	renderer.end();
	for (int i = 0; i < items.size(); i++) {
	    renderer.begin(ShapeRenderer.ShapeType.FilledRectangle);
	    ContextMenuItem contextMenuItem = items.get(i);
	    final int currentPosition = (int) (i * font.getLineHeight());
	    if (i == selected) {
		renderer.setColor(Color.WHITE);
		if (contextMenuItem instanceof ContextSubMenu) {
		    renderer.filledRect(maxWidth - 10, currentPosition + ((int) font.getLineHeight() >> 1) - 1, 3, 3);
		    ContextSubMenu subMenu = (ContextSubMenu) contextMenuItem;
//		    g.popTransform();
		    subMenu.render(batch);
//		    g.pushTransform();
//		    g.translate(x, y);
		}
		font.setColor(Color.WHITE);
	    } else {
		renderer.setColor(Color.BLACK);
		if (contextMenuItem instanceof ContextSubMenu) {
		    renderer.filledRect(maxWidth - 10, currentPosition + ((int) font.getLineHeight() >> 1) - 1, 3, 3);
		}
		font.setColor(Color.BLACK);
	    }
	    renderer.end();
	    batch.begin();
	    font.draw(batch, contextMenuItem.getLabel(), x, y + currentPosition);
	    batch.end();
	}
    }

    public boolean add(String str) {
	return add(new ContextMenuItem(str));
    }

    public boolean add(ContextMenuItem e) {
	if (font == null) {
	    throw new RuntimeException("Sub menu should be added to Context menu before items could be added to it.");
	}
	int width = (int) font.getBounds((e.getLabel())).width;
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
	    subMenu.setY((int) (y + items.size() * font.getLineHeight()));
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
	    selected = y / (int) font.getLineHeight();
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
		int selected = y / (int) font.getLineHeight();
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
