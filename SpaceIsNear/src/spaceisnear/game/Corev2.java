// Generated by delombok at Sun Dec 01 13:28:22 MSK 2013
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */// Generated by delombok at Sun Dec 01 13:28:22 MSK 2013
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game;

import spaceisnear.game.ui.console.GameConsole;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import spaceisnear.AbstractGameObject;
import spaceisnear.game.components.client.PaintableComponent;
import spaceisnear.game.ui.console.LogLevel;
import spaceisnear.game.ui.console.LogString;
import spaceisnear.game.messages.MessageControlledByInput;
import spaceisnear.game.messages.properties.MessagePropertySet;
import spaceisnear.game.messages.MessageTimePassed;
import spaceisnear.game.messages.MessageToSend;
import spaceisnear.game.objects.NetworkingObject;
import spaceisnear.game.objects.items.ItemsArchive;
import spaceisnear.game.objects.items.ItemsReader;
import spaceisnear.game.ui.*;
import spaceisnear.game.objects.items.StaticItem;

/**
 * @author LPzhelud
 */
public class Corev2 extends BasicGameState {

    private GameContext context;
    private final ArrayList<AbstractGameObject> objects = new ArrayList<>();
    private static final int QUANT_TIME = 50;
    private int key;
    public static String IP;
    @Getter private boolean notpaused;
    private GameConsole console;
    private ContextMenu menu;

    @Override
    public void init(GameContainer container, StateBasedGame sbg) throws SlickException {
	try {
	    ItemsArchive.itemsArchive = new ItemsArchive(ItemsReader.read());
	} catch (Exception ex) {
	    Logger.getLogger(Corev2.class.getName()).log(Level.SEVERE, null, ex);
	}
	context = new GameContext(new CameraMan(), objects, this);
	context.addObject(new NetworkingObject(context));
	context.checkSize();
	context.getCameraMan().setWindowWidth(800);
	context.getCameraMan().setWindowHeight(600);
	context.getCameraMan().delegateWidth();
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
	console = new GameConsole(800, 0, 400, 600, container, context);
	context.setCameraToPlayer();
    }

    public void callToConnect() {
	try {
	    context.getNetworking().connect(IP, 54555);
	} catch (IOException ex) {
	    Logger.getLogger(Corev2.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    @Override
    public void update(GameContainer container, StateBasedGame sbg, int delta) throws SlickException {
	if (notpaused) {
	    MessageControlledByInput mc = checkKeys();
	    //1 quant of time is 50L by default
//	    int quants = delta / QUANT_TIME;
	    MessageTimePassed messageTimePassed = new MessageTimePassed(delta);
	    context.sendThemAll(messageTimePassed);
	    //
	    int playerID = context.getPlayerID();
	    if (mc != null && playerID != -1) {
		context.sendDirectedMessage(new MessageToSend(mc));
	    }
	    for (AbstractGameObject gameObject : objects) {
		gameObject.process();
	    }
	}
    }

    @Override
    public void keyPressed(int key, char c) {
	this.key = key;
    }

    @Override
    public void keyReleased(int key, char c) {
	this.key = 0;
    }

    private MessageControlledByInput checkKeys() {
	MessageControlledByInput mc = null;
	boolean ableToMove = !context.getPlayer().getPositionComponent().isAnimation() && !console.hasFocus();
	switch (key) {
	    case Input.KEY_UP:
		if (ableToMove) {
		    mc = new MessageControlledByInput(MessageControlledByInput.Type.UP, context.getPlayerID());
		}
		break;
	    case Input.KEY_DOWN:
		if (ableToMove) {
		    mc = new MessageControlledByInput(MessageControlledByInput.Type.DOWN, context.getPlayerID());
		}
		break;
	    case Input.KEY_LEFT:
		if (ableToMove) {
		    mc = new MessageControlledByInput(MessageControlledByInput.Type.LEFT, context.getPlayerID());
		}
		break;
	    case Input.KEY_RIGHT:
		if (ableToMove) {
		    mc = new MessageControlledByInput(MessageControlledByInput.Type.RIGHT, context.getPlayerID());
		}
		break;
	    case Input.KEY_ESCAPE:
		MessagePropertySet messagePropertySet = new MessagePropertySet(((GameContext) context).getPlayerID(), "pull", -1);
		MessageToSend messageToSend = new MessageToSend(messagePropertySet);
		context.sendDirectedMessage(messageToSend);
		break;
	}
	return mc;
    }

    @Override
    public void render(GameContainer container, StateBasedGame sbg, Graphics g) throws SlickException {
	g.scale(GameContext.SCALING_X, GameContext.SCALING_Y);
	g.pushTransform();
	context.getCameraMan().moveCamera(g);
	context.getCameraMan().paint(g);
	for (PaintableComponent paintableComponent : context.getPaintables()) {
	    paintableComponent.paint(g);
	}
//	context.getCameraMan().unmoveCamera(g);
	g.popTransform();
	console.paint(g, container);
	if (menu != null) {
	    menu.render(g);
	}
    }

    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
	if (menu != null) {
	    menu.mouseMoved(newx, newy);
	}
    }

    @Override
    public int getID() {
	return 3;
    }

    public void pause() {
	notpaused = false;
    }

    public void unpause() {
	notpaused = true;
	System.out.println("Client has continued his work");
    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
	if (console.intersects(x, y)) {
	    console.mouseClicked(button, x, y, clickCount);
	} else {
	    int toAddX = context.getCameraMan().getX();
	    int toAddY = context.getCameraMan().getY();
	    int calculatedX = x / GameContext.TILE_WIDTH;
	    int calculatedY = y / GameContext.TILE_HEIGHT;
	    int tileX = toAddX + calculatedX;
	    int tileY = toAddY + calculatedY;
	    if (tileX < 0 || tileY < 0) {
		return;
	    }
	    console.pushMessage(new LogString("Clicked: x " + tileX + " y " + tileY + " button " + button, LogLevel.DEBUG));
	    if (button == 1) {
		if (menu == null) {
		    createContextMenuWithItems(x, y, tileX, tileY);
		} else {
		    menu = null;
		}
	    } else if (button == 0) {
		if (menu != null) {
		    menu.mouseClicked(button, x, y, clickCount);
		}
	    }
	}
    }

    private void createContextMenuWithItems(int x, int y, int tileX, int tileY) {
	ContextMenu contextMenu = new ContextMenu(x, y, console.getFont());
	List<AbstractGameObject> itemsOn = context.itemsOn(tileX, tileY);
	for (AbstractGameObject staticItem : itemsOn) {
	    final StaticItem item = (StaticItem) staticItem;
	    ContextSubMenu contextSubMenu = new ContextSubMenu(item.getProperties().getName());
	    contextMenu.add(contextSubMenu);
	    contextSubMenu.add("Learn");
	    contextSubMenu.add("Pull");
	    contextSubMenu.add("Take");
	    contextSubMenu.setActionListener(new ActionListener() {

		@Override
		public void itemActivated(ContextMenuItem e) {
		    switch (e.getLabel()) {
			case "Learn":
			    String description = item.getProperties().getDescription();
			    console.pushMessage(new LogString(description, LogLevel.TALKING));
			    break;
			case "Pull":
			    int id = item.getId();
			    int playerID = context.getPlayerID();
			    MessagePropertySet messagePropertySet = new MessagePropertySet(playerID, "pull", id);
			    MessageToSend messageToSend = new MessageToSend(messagePropertySet);
			    context.sendDirectedMessage(messageToSend);
			    break;
		    }
		    menu = null;
		}
	    });
	}
	menu = contextMenu;
//	testMenu(x, y);
    }

    private void testMenu(int x, int y) {
	ContextMenu contextMenu = new ContextMenu(x, y, console.getFont());
	contextMenu.add("lah");
	contextMenu.add("contextmenu so cool");
	contextMenu.add("pokemons");
	final ContextSubMenu contextSubMenu = new ContextSubMenu("loh");
	contextMenu.add(contextSubMenu);
	contextSubMenu.add("test");
	contextSubMenu.add("test2");
	menu = contextMenu;
    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
	console.mouseDragged(oldx, oldy, newx, newy);
    }

    public void log(LogString log) {
	if (console != null) {
	    console.pushMessage(log);
	}
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
	console.mouseReleased(button, x, y);
    }

    @Override
    public void mousePressed(int button, int x, int y) {
	console.mousePressed(button, x, y);
    }

    @Override
    public void mouseWheelMoved(int newValue) {
	console.mouseWheelMoved(newValue);
    }

}
