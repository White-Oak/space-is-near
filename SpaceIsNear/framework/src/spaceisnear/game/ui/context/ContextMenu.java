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
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.*;
import java.util.*;
import lombok.*;
import spaceisnear.game.ui.ActivationListener;
import spaceisnear.game.ui.UIElement;

/**
 *
 * @author White Oak
 */
public final class ContextMenu extends UIElement implements ContextMenuItemable {

    @Getter private String label;
    private final List<ContextMenuItemable> items = new ArrayList<>();
    private int maxWidth;
    private int height;
    @Getter private int selected;
    private final Stage stage;
    public final static int HEIGHT_PADDING = 5;

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
		ContextMenu.this.mouseClicked(button, (int) x, (int) y);
		return true;
	    }

	});
    }
    private final static ShapeRenderer renderer = new ShapeRenderer();

    @Override
    public void paint(Batch batch) {
	render(batch);
    }

    private int getLineHeight() {
	return (int) (font.getLineHeight() + HEIGHT_PADDING);
    }

    public void render(Batch batch) {
	renderer.setProjectionMatrix(batch.getProjectionMatrix());
	renderer.translate(getX(), getY(), 0);
	renderer.begin(ShapeRenderer.ShapeType.Filled);
	{
	    renderer.setColor(Color.WHITE);
	    renderer.rect(0, 0, maxWidth, height);
	    renderer.setColor(Color.BLACK);
	    renderer.rect(0, selected * getLineHeight(), maxWidth, getLineHeight());
	}
	renderer.end();
	renderer.begin(ShapeRenderer.ShapeType.Line);
	{
	    renderer.rect(-1, -1, maxWidth + 1, height + 1);
	}
	renderer.end();
	for (int i = 0; i < items.size(); i++) {
	    ContextMenuItemable contextMenuItem = items.get(i);
	    final int currentPosition = (i * getLineHeight());
	    renderer.begin(ShapeRenderer.ShapeType.Filled);
	    {
		Color color = i == selected ? Color.WHITE : Color.BLACK;
		renderer.setColor(color);
		font.setColor(color);
		if (contextMenuItem instanceof ContextMenu) {
		    renderer.rect(maxWidth - 10, currentPosition + (getLineHeight() >> 1) - 1, 3, 3);
		}
	    }
	    renderer.end();
	    batch.begin();
	    {
		font.draw(batch, contextMenuItem.getLabel(), getX(), getY() + currentPosition + HEIGHT_PADDING / 2);
	    }
	    batch.end();
	}
	renderer.translate(-getX(), -getY(), 0);
    }

    public boolean add(String str) {
	return add(new ContextMenuItem(str));
    }

    private void recalculateWidth(int width) {
	if (width > maxWidth) {
	    maxWidth = width + 20;
	    setX(getX());
	}
    }

    public boolean add(ContextMenuItemable e) {
	int width = (int) font.getBounds((e.getLabel())).width;
	//Increasing width of this menu if label of new item is too big
	recalculateWidth(width);
	//Increasing height by line
	height += font.getLineHeight() + HEIGHT_PADDING;
	//Setting position for new menu
	if (e instanceof ContextMenu) {
	    ContextMenu subMenu = (ContextMenu) e;
	    subMenu.setX(getX() + maxWidth + 1);
	    subMenu.setY((int) (getY() + items.size() * getLineHeight()));
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
	collapseShownMenus();
	stage.getActors().removeValue(this, true);
    }

    public void show() {
	stage.addActor(this);
	selected = 0;
	selectedItem = items.get(selected);
	if (selectedItem instanceof ContextMenu) {
	    ((ContextMenu) selectedItem).show();
	}
    }

    public void setLabel(String label) {
	this.label = label;
	int width = (int) font.getBounds((label)).width;
	recalculateWidth(width);
    }

    @Override
    public void setX(float x) {
	super.setX(x); //To change body of generated methods, choose Tools | Templates.
	items.stream()
		.filter((contextMenuItem) -> (contextMenuItem instanceof ContextMenu))
		.map((contextMenuItem) -> (ContextMenu) contextMenuItem)
		.forEach((subMenu) -> subMenu.setX(getX() + maxWidth + 1));
    }

    @Override
    public void setY(float y) {
	super.setY(y); //To change body of generated methods, choose Tools | Templates.
	for (int i = 0; i < items.size(); i++) {
	    ContextMenuItemable contextMenuItemable = items.get(i);
	    if (contextMenuItemable instanceof ContextMenu) {
		ContextMenu menu = (ContextMenu) contextMenuItemable;
		menu.setY(getY() + i * getLineHeight());
	    }
	}
    }

    public synchronized void setItems(List<List<ContextMenuItemable>> actions, List<ActivationListener> listeners) {
	if (actions == null || listeners == null) {
	    throw new IllegalArgumentException("Argumnents can't be null!");
	}
	if (actions.size() != items.size()) {
	    throw new IllegalArgumentException("New list should be of the same length as a previous");
	}
	collapseShownMenus();
	//Making temporary copy of old list to save items' names
	List<ContextMenuItemable> temporary = new ArrayList<>(items);
	//Clearing old list
	items.clear();
	maxWidth = height = 0;
	addAllMenus(actions, temporary, listeners);
    }

    private void collapseShownMenus() {
	//hiding what was shown
	if (selectedItem instanceof ContextMenu) {
	    ContextMenu current = (ContextMenu) selectedItem;
	    current.hide();
	}
    }

    private void addAllMenus(List<List<ContextMenuItemable>> actions, List<ContextMenuItemable> temporary,
			     List<ActivationListener> listeners) {
	for (int i = 0; i < temporary.size(); i++) {
	    final ContextMenu contextMenu = new ContextMenu(temporary.get(i).getLabel(), stage);
	    add(contextMenu);
	    //adding all actions to the submenu
	    actions.get(i).forEach(contextMenu::add);
	    contextMenu.setActivationListener(listeners.get(i));
	}
    }

}
