package spaceisnear.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import java.io.IOException;
import java.util.*;
import java.util.logging.*;
import lombok.*;
import spaceisnear.abstracts.AbstractGameObject;
import spaceisnear.game.components.client.PaintableComponent;
import spaceisnear.game.messages.*;
import spaceisnear.game.messages.properties.MessagePropertySet;
import spaceisnear.game.objects.NetworkingObject;
import spaceisnear.game.objects.items.*;
import spaceisnear.game.ui.UIListener;
import spaceisnear.game.ui.console.*;
import spaceisnear.game.ui.context.*;
import spaceisnear.game.ui.inventory.Inventory;

/**
 * @author LPzhelud
 */
public class Corev2 implements Screen, Runnable, UIListener {

    private GameContext context;
    private final ArrayList<AbstractGameObject> objects = new ArrayList<>();
    @Setter private int key;
    public static String IP;
    @Getter private boolean notpaused;
    private Stage stage;
    private GameConsole console;
    private ContextMenu menu;
    private Inventory inventory;
    @Getter private BitmapFont font;
    private final OrthographicCamera camera = new OrthographicCamera(1200, 600);

    public void init() {
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
	camera.setToOrtho(true, 1200, 600);
	camera.update();
	stage = new Stage();
	stage.setCamera(camera);
	font = new BitmapFont(Gdx.files.classpath("default.fnt"), false);
	final spaceisnear.game.ui.TextField textField = new spaceisnear.game.ui.TextField();
	console = new GameConsole(800, 0, 400, 600, context, textField);
	textField.setUIListener(this);
//	stage.addActor(table);
	console.setPosition(800, 0);
	stage.addActor(console);
	textField.setBounds(800, Gdx.graphics.getHeight() - textField.getPrefHeight(), 400, textField.getPrefHeight());
	stage.addActor(textField);
	inputCatcher = new InputCatcher(this);
	inputCatcher.setBounds(0, 0, 800, 600);
	stage.addActor(inputCatcher);
	stage.setKeyboardFocus(inputCatcher);
	camera.setToOrtho(true);
    }
    private InputCatcher inputCatcher;

    public void callToConnect() {
	try {
	    context.getNetworking().connect(IP, 54555);
	} catch (IOException ex) {
	    Logger.getLogger(Corev2.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    public void update(int delta) {
	if (notpaused) {
	    MessageControlledByInput mc = checkKeys();
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

    public void keyPressed(int key, char c) {
	this.key = key;
    }

    public void keyReleased(int key, char c) {
	this.key = 0;
    }

    private MessageControlledByInput checkKeys() {
	MessageControlledByInput mc = null;
	boolean ableToMove = !context.getPlayer().getPositionComponent().isAnimated() && !console.hasFocus();
	switch (key) {
	    case Input.Keys.UP:
		if (ableToMove) {
		    mc = new MessageControlledByInput(MessageControlledByInput.Type.UP, context.getPlayerID());
		}
		break;
	    case Input.Keys.DOWN:
		if (ableToMove) {
		    mc = new MessageControlledByInput(MessageControlledByInput.Type.DOWN, context.getPlayerID());
		}
		break;
	    case Input.Keys.LEFT:
		if (ableToMove) {
		    mc = new MessageControlledByInput(MessageControlledByInput.Type.LEFT, context.getPlayerID());
		}
		break;
	    case Input.Keys.RIGHT:
		if (ableToMove) {
		    mc = new MessageControlledByInput(MessageControlledByInput.Type.RIGHT, context.getPlayerID());
		}
		break;
	    case Input.Keys.ESCAPE:
		MessagePropertySet messagePropertySet = new MessagePropertySet(((GameContext) context).getPlayerID(), "pull", -1);
		MessageToSend messageToSend = new MessageToSend(messagePropertySet);
		context.sendDirectedMessage(messageToSend);
		break;
	    case Input.Keys.M:
		inventory.setMinimized(!inventory.isMinimized());
		break;
	}
	return mc;
    }
    private final SpriteBatch batch = new SpriteBatch();

    @Override
    public void render(float delta) {
	Gdx.gl.glClearColor(0, 0, 0, 1);
	Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	context.getCameraMan().moveCamera();
	batch.setProjectionMatrix(context.getCameraMan().getCamera().combined);
	batch.begin();
	for (PaintableComponent paintableComponent : context.getPaintables()) {
	    paintableComponent.paint(batch);
	}
	batch.end();
	stage.draw();
	context.getCameraMan().unmoveCamera();
    }

    public int getID() {
	return 3;
    }

    @Override
    public void pause() {
	notpaused = false;
    }

    public void unpause() {
	notpaused = true;
	System.out.println("Client has continued his work");
    }

    public void mouseClicked(int button, int x, int y, int clickCount) {
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
		menu.hide();
		menu = null;
	    }
	}
    }

    private void createContextMenuWithItems(int x, int y, int tileX, int tileY) {
	ContextMenu contextMenu = new ContextMenu(null, stage);
	contextMenu.setPosition(x, y);
	java.util.List<AbstractGameObject> itemsOn = context.itemsOn(tileX, tileY);
	for (AbstractGameObject staticItem : itemsOn) {
	    final StaticItem item = (StaticItem) staticItem;
	    ContextMenu contextSubMenu = new ContextMenu(item.getProperties().getName(), stage);
	    contextMenu.add(contextSubMenu);
	    contextSubMenu.add("Learn");
	    contextSubMenu.add("Pull");
	    contextSubMenu.add("Take");
	    contextSubMenu.setActionListener(new ActionListener() {

		@Override
		public void itemActivated(ContextMenuItemable e) {
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
		    menu.hide();
		    menu = null;
		}
	    });
	}
	menu = contextMenu;
	stage.addActor(menu);
//	testMenu(x, y);
    }

    public void log(LogString log) {
	if (console != null) {
	    console.pushMessage(log);
	}
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
	update(20);
	context.setCameraToPlayer();
	Gdx.input.setInputProcessor(stage);
	new Thread(this).start();
    }

    @Override
    public void hide() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

    @Override
    public void run() {
	while (true) {
	    update(50);
	    try {
		Thread.sleep(50);
	    } catch (InterruptedException ex) {
		Logger.getLogger(Corev2.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
    }

    @Override
    public void componentActivated(Actor actor) {
	console.processInputedMessage();
	stage.setKeyboardFocus(inputCatcher);
    }

}
