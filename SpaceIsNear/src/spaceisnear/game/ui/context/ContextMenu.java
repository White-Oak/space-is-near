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
import spaceisnear.game.GameContext;
import spaceisnear.game.components.server.context.ServerContextMenu;
import spaceisnear.game.components.server.context.ServerContextSubMenu;
import spaceisnear.game.messages.MessageActionChosen;
import spaceisnear.game.messages.MessageToSend;
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
    public void paint(SpriteBatch batch) {
	render(batch);
    }

    private int getLineHeight() {
	return (int) (font.getLineHeight() + HEIGHT_PADDING);
    }

    public void render(SpriteBatch batch) {
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
	if (selectedItem instanceof ContextMenu) {
	    ContextMenu menu = (ContextMenu) selectedItem;
	    menu.hide();
	}
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

    public synchronized void setItems(ServerContextMenu menu, GameContext context) {
	assert menu != null;
	final ServerContextSubMenu[] subMenus = menu.getSubMenus();
	assert subMenus != null;
	assert items.size() == subMenus.length;
	//hiding what was shown
	if (selectedItem instanceof ContextMenu) {
	    ContextMenu current = (ContextMenu) selectedItem;
	    current.hide();
	}
	//Making temporary copy of old list to save items' names
	ArrayList<ContextMenuItemable> temporary = new ArrayList<>(items);
	//Clearing old list
	items.clear();
	maxWidth = height = 0;
	//Adding all menus
	for (int i = 0; i < subMenus.length; i++) {
	    ServerContextSubMenu serverContextSubMenu = subMenus[i];
	    ContextMenu contextMenu = new ContextMenu(temporary.get(i).getLabel(), stage);
	    add(contextMenu);
	    for (int j = 0; j < serverContextSubMenu.getActions().length; j++) {
		String string = serverContextSubMenu.getActions()[j];
		contextMenu.add(string);
	    }

	    //Creating new listeners
	    final int currentIndex = i;
	    contextMenu.setActivationListener(element -> {
		MessageActionChosen messageActionChosen;
		messageActionChosen = new MessageActionChosen(contextMenu.getSelected(), currentIndex);
		MessageToSend messageToSend = new MessageToSend(messageActionChosen);
		context.sendDirectedMessage(messageToSend);
		context.menuWantsToHide();
	    });
	}
	//to prevent overflowing
	if (selected >= items.size()) {
	    selected = items.size() - 1;
	}
	System.out.println("Done!");
    }

}
