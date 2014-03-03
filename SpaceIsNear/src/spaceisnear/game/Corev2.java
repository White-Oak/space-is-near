package spaceisnear.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import java.io.IOException;
import java.util.*;
import java.util.logging.*;
import lombok.*;
import spaceisnear.Main;
import spaceisnear.abstracts.AbstractGameObject;
import spaceisnear.game.components.client.PaintableComponent;
import spaceisnear.game.components.inventory.Inventory;
import spaceisnear.game.messages.*;
import spaceisnear.game.messages.properties.MessagePropertySet;
import spaceisnear.game.objects.NetworkingObject;
import spaceisnear.game.objects.items.*;
import spaceisnear.game.ui.console.*;
import spaceisnear.game.ui.context.*;
import spaceisnear.starting.ui.ScreenImprovedGreatly;

/**
 * @author LPzhelud
 */
public final class Corev2 extends ScreenImprovedGreatly implements Runnable {

    @Getter private GameContext context;
    private final ArrayList<AbstractGameObject> objects = new ArrayList<>();
    @Setter private int key;
    public static String IP = "127.0.0.1";
    @Getter private boolean notpaused;

    @Getter private final Networking networking;
    private ContextMenu menu;
    private final OrthographicCamera camera = new OrthographicCamera(1200, 600);
    private InputCatcher inputCatcher;
    private Inventory inventory;

    private final static MessageAnimationStep MESSAGE_ANIMATION_STEP = new MessageAnimationStep();

    public Corev2(Corev3 corev3) {
	super(corev3);
	networking = new Networking(this);
	init();
    }

    public void init() {
	try {
	    ItemsArchive.itemsArchive = new ItemsArchive(ItemsReader.read());
	} catch (Exception ex) {
	    Logger.getLogger(Corev2.class.getName()).log(Level.SEVERE, null, ex);
	}

	context = new GameContext(new CameraMan(), objects, this);
	context.addObject(new NetworkingObject(networking));
	context.checkSize();
	context.getCameraMan().setWindowWidth(800);
	context.getCameraMan().setWindowHeight(600);

	camera.setToOrtho(true, 1200, 600);
	camera.update();
	stage.setCamera(camera);

	inputCatcher = new InputCatcher(this);
	inputCatcher.setBounds(0, 0, 800, 600);
	stage.addActor(inputCatcher);
	stage.setKeyboardFocus(inputCatcher);

	inventory = new Inventory(context);
	stage.addActor(inventory);

	callToConnect();
    }

    private void callToConnect() {
	new Thread() {
	    @Override
	    public void run() {
		try {
		    networking.connect(IP, 54555);
		} catch (IOException ex) {
		    Main.main(new String[]{"host"});
		    synchronized (Corev2.this) {
			try {
			    log(new LogString("Couldn't find a host on " + IP, LogLevel.WARNING));
			    IP = "127.0.0.1";
			    log(new LogString("Starting server on " + IP, LogLevel.WARNING));
			    networking.connect(IP, 54555);
			} catch (IOException ex1) {
			    Logger.getLogger(Corev2.class.getName()).log(Level.SEVERE, null, ex1);
			}
		    }
		}
	    }
	}.start();
    }

    private void animate() {
	context.sendThemAll(MESSAGE_ANIMATION_STEP);
	inventory.processMessage(MESSAGE_ANIMATION_STEP);
    }

    public void update(int delta) {
	if (notpaused) {
//	    MessageTimePassed messageTimePassed = new MessageTimePassed(delta);
//	    context.sendThemAll(messageTimePassed);
	    //
	    checkKeys();
	    MessageControlledByInput mc = checkMovementDesired();
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
    private long lastTimeMoved;
    private final static long MINIMUM_TIME_TO_MOVE = 60L;

    private MessageControlledByInput checkMovementDesired() {
	MessageControlledByInput mc = null;
	boolean ableToMove = !context.getPlayer().getPositionComponent().isAnimated() && !getConsole().hasFocus()
		&& System.currentTimeMillis() - lastTimeMoved > MINIMUM_TIME_TO_MOVE;
	if (ableToMove) {
	    switch (key) {
		case Input.Keys.UP:
		    mc = new MessageControlledByInput(MessageControlledByInput.Type.UP, context.getPlayerID());
		    break;
		case Input.Keys.DOWN:
		    mc = new MessageControlledByInput(MessageControlledByInput.Type.DOWN, context.getPlayerID());
		    break;
		case Input.Keys.LEFT:
		    mc = new MessageControlledByInput(MessageControlledByInput.Type.LEFT, context.getPlayerID());
		    break;
		case Input.Keys.RIGHT:
		    mc = new MessageControlledByInput(MessageControlledByInput.Type.RIGHT, context.getPlayerID());
		    break;
		case Input.Keys.M:
		    inventory.setMinimized(!inventory.isMinimized());
		    lastTimeMoved = System.currentTimeMillis();
		    break;
	    }
	    if (mc != null) {
		lastTimeMoved = System.currentTimeMillis();
	    }
	}
	return mc;
    }

    private void checkKeys() {
	switch (key) {
	    case Input.Keys.ESCAPE:
		MessagePropertySet messagePropertySet = new MessagePropertySet(((GameContext) context).getPlayerID(), "pull", -1);
		MessageToSend messageToSend = new MessageToSend(messagePropertySet);
		context.sendDirectedMessage(messageToSend);
		break;
	    case Input.Keys.ENTER:
		final boolean focused = getConsole().getTextField().isFocused();
		if (!focused) {
		    key = 0;
		    stage.setKeyboardFocus(getConsole().getTextField());
		}
		break;
	}
    }
    private final SpriteBatch batch = new SpriteBatch();

    @Override
    public void draw() {
	context.getCameraMan().moveCamera();
	batch.setProjectionMatrix(context.getCameraMan().getCamera().combined);
	batch.begin();
	for (PaintableComponent paintableComponent : context.getPaintables()) {
	    paintableComponent.paint(batch);
	}
	batch.end();
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
	log(new LogString("Clicked: x " + tileX + " y " + tileY + " button " + button, LogLevel.DEBUG));
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
			    log(new LogString(description, LogLevel.TALKING));
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
	if (getConsole() != null) {
	    getConsole().pushMessage(log);
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
	new Thread(new Runnable() {

	    @Override
	    public void run() {
		while (true) {
		    animate();
		    try {
			Thread.sleep(MessageAnimationStep.STEP);
		    } catch (InterruptedException ex) {
			Logger.getLogger(Corev2.class.getName()).log(Level.SEVERE, null, ex);
		    }
		}
	    }
	}).start();
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
}
