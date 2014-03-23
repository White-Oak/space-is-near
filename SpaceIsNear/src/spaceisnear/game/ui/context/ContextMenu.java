/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.ui.context;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import spaceisnear.game.ui.ActivationListener;
import spaceisnear.game.ui.UIElement;

/**
 *
 * @author White Oak
 */
public final class ContextMenu extends UIElement implements ContextMenuItemable {

    @Getter private final String label;
    private final List<ContextMenuItemable> items = new ArrayList<>();
    private int maxWidth;
    private int height;
    @Getter private int selected;
    private final Stage stage;

    public ContextMenu(String label, Stage stage) {
	this.label = label;
	this.stage = stage;
	init();
    }

    public ContextMenu(String label, Stage stage, int x, int y) {
	this(label, stage);
	setPosition(x, y);
    }

    public void init() {
	addCaptureListener(new InputListener() {

	    @Override
	    public boolean mouseMoved(InputEvent event, float x, float y) {
		ContextMenu.this.mouseMoved((int) x, (int) y);
		return true;
	    }

	    @Override
	    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		System.out.println("ohoh");
		ContextMenu.this.mouseClicked(button, (int) x, (int) y);
		return true;
	    }

	});
    }
    private final ShapeRenderer renderer = new ShapeRenderer();

    @Override
    public void paint(SpriteBatch batch) {
	render(batch);
    }

    public void render(SpriteBatch batch) {
	renderer.setProjectionMatrix(batch.getProjectionMatrix());
	renderer.translate(getX(), getY(), 0);
	renderer.begin(ShapeRenderer.ShapeType.FilledRectangle);
	{
	    renderer.setColor(Color.WHITE);
	    renderer.filledRect(0, 0, maxWidth, height);
	    renderer.setColor(Color.BLACK);
	    renderer.filledRect(0, selected * font.getLineHeight(), maxWidth, font.getLineHeight());
	}
	renderer.end();
	renderer.begin(ShapeRenderer.ShapeType.Rectangle);
	{
	    renderer.rect(-1, -1, maxWidth + 1, height + 1);
	}
	renderer.end();
	for (int i = 0; i < items.size(); i++) {
	    ContextMenuItemable contextMenuItem = items.get(i);
	    final int currentPosition = (int) (i * font.getLineHeight());
	    renderer.begin(ShapeRenderer.ShapeType.FilledRectangle);
	    {
		Color color = i == selected ? Color.WHITE : Color.BLACK;
		renderer.setColor(color);
		font.setColor(color);
		if (contextMenuItem instanceof ContextMenu) {
		    renderer.filledRect(maxWidth - 10, currentPosition + ((int) font.getLineHeight() >> 1) - 1, 3, 3);
		}
	    }
	    renderer.end();
	    batch.begin();
	    {
		font.draw(batch, contextMenuItem.getLabel(), getX(), getY() + currentPosition);
	    }
	    batch.end();
	}
	renderer.translate(-getX(), -getY(), 0);
    }

    public boolean add(String str) {
	return add(new ContextMenuItem(str));
    }

    public boolean add(ContextMenuItemable e) {
	int width = (int) font.getBounds((e.getLabel())).width;
	//Increasing width of this menu if label of new item is too big
	if (width > maxWidth) {
	    maxWidth = width + 20;
	    items.stream()
		    .filter((contextMenuItem) -> (contextMenuItem instanceof ContextMenu))
		    .map((contextMenuItem) -> (ContextMenu) contextMenuItem)
		    .forEach((subMenu) -> subMenu.setX(getX() + maxWidth + 1));
	}
	//Increasing height by line
	height += font.getLineHeight();
	//Setting position for new menu
	if (e instanceof ContextMenu) {
	    ContextMenu subMenu = (ContextMenu) e;
	    subMenu.setX(getX() + maxWidth + 1);
	    subMenu.setY((int) (getY() + items.size() * font.getLineHeight()));
	    subMenu.setActivationListener(getActivationListener());
	}
	setWidth(getWidth(font));
	setHeight(getHeight(font));
	setBounds(getX(), getY(), getWidth(), getHeight());
	return items.add(e);
    }

    public void mouseMoved(int x, int y) {
	final int selectedTemp = y / (int) font.getLineHeight();
	if (selectedTemp >= 0 && selectedTemp < items.size()) {
	    selected = selectedTemp;
	    if (items.get(selected) != selectedItem) {
		if (items.get(selected) instanceof ContextMenu) {
		    stage.getActors().removeValue((Actor) selectedItem, true);
		    ContextMenu get = (ContextMenu) items.get(selected);
		    stage.addActor(get);
		}
		selectedItem = items.get(selected);
	    }
	}
    }
    private ContextMenuItemable selectedItem;

    public void mouseClicked(int button, int x, int y) {
	if (getActivationListener() != null) {
	    if (selectedItem instanceof ContextMenuItem) {
		getActivationListener().componentActivated(this);
	    }
	}
    }

    @Override
    public void setActivationListener(ActivationListener activationListener) {
	super.setActivationListener(activationListener);
	items.stream()
		.filter(e -> e instanceof ContextMenu)
		.map(e -> (ContextMenu) e)
		.forEach(e -> e.setActivationListener(activationListener));
    }

    public int getWidth(BitmapFont font) {
	int totalWidth = maxWidth;
//	for (ContextMenuItemable contextMenuItem : items) {
//	    totalWidth += contextMenuItem.getWidth(font);
//	}
	return totalWidth;
    }

    public int getHeight(BitmapFont font) {
	return height;
    }

    public void hide() {
	if (selectedItem instanceof ContextMenu) {
	    ContextMenu menu = (ContextMenu) selectedItem;
	    menu.hide();
	}
	stage.getActors().removeValue(this, true);
    }

}
