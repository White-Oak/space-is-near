package spaceisnear.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import java.util.*;
import lombok.Setter;

public class MenuBar extends UIElement {

    private final List<MenuItem> items = new ArrayList<>();
    private final static int BAR_HEIGHT = (int) font.getLineHeight() + 10;
    @Setter private Color color = Color.WHITE;
    private MenuItem selected;
    private boolean activated;
    @Setter private Color textColor = Color.BLACK;

    public MenuBar() {
	addCaptureListener(new InputListener() {

	    @Override
	    public boolean mouseMoved(InputEvent event, float x, float y) {
		MenuBar.this.mouseMoved((int) x);
		return true;
	    }

	    @Override
	    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		MenuBar.this.mouseClicked((int) x);
		return true;
	    }

	});
    }

    @Override
    public float getPrefHeight() {
	return BAR_HEIGHT;
    }

    public boolean add(MenuItem e) {
	return items.add(e);
    }

    @Override
    public void paint(SpriteBatch batch) {
	ShapeRenderer renderer = getRenderer();
	renderer.setColor(color);
	renderer.begin(ShapeRenderer.ShapeType.FilledRectangle);
	{
	    renderer.filledRect(0, 0, getWidth(), getHeight());
	}
	renderer.end();

	int currentx = 0;
	for (MenuItem menuItem : items) {
	    if (currentx < getWidth()) {
		currentx += menuItem.paint((int) getX() + currentx, (int) getY() + 5, renderer);
	    }
	}

	batch.begin();
	{
	    font.setColor(textColor);
	    currentx = 0;
	    for (MenuItem menuItem : items) {
		if (currentx < getWidth()) {
		    font.draw(batch, menuItem.getLabel(), getX() + currentx + 10, getY() + 5);
		    currentx += menuItem.getWidth();
		}
	    }
	}
	batch.end();
    }

    private void mouseMoved(int x) {
	MenuItem menuItem = getItemOn(x);
	if (menuItem != null) {
	    if (activated) {
		if (menuItem != selected) {
		    menuItem.select(getXFor(menuItem), (int) (getY() + getHeight()));
		    if (selected != null) {
			selected.unselect();
		    }
		    selected = menuItem;
		}
	    }
	}
    }

    private int getXFor(MenuItem item) {
	Objects.requireNonNull(item);
	int currentx = 0;
	for (int i = 0; i < items.size(); i++) {
	    MenuItem menuItem = items.get(i);
	    if (menuItem == item) {
		return currentx;
	    }
	    currentx += menuItem.getWidth();
	}
	return -1;
    }

    private MenuItem getItemOn(int x) {
	if (x < getWidth()) {
	    int currentx = 0;
	    for (int i = 0; i < items.size(); i++) {
		MenuItem menuItem = items.get(i);
		currentx += menuItem.getWidth();
		if (currentx > x) {
		    return menuItem;
		}
	    }
	}
	return null;
    }

    private void mouseClicked(int x) {
	activated = !activated;
	if (activated) {
	    mouseMoved(x);
	} else {
	    if (selected != null) {
		selected.unselect();
	    }
	}
    }
}
