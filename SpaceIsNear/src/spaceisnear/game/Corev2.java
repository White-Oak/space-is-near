package spaceisnear.game;

import box2dLight.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import java.io.*;
import java.util.*;
import lombok.*;
import org.apache.commons.cli.ParseException;
import spaceisnear.*;
import spaceisnear.abstracts.*;
import spaceisnear.game.components.inventory.Inventory;
import spaceisnear.game.messages.*;
import spaceisnear.game.messages.properties.MessagePropertySet;
import spaceisnear.game.objects.NetworkingObject;
import spaceisnear.game.objects.items.*;
import spaceisnear.game.ui.UIElement;
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
    @Getter private boolean notpaused;

    @Getter private final Networking networking;
    private ContextMenu menu;
    private final OrthographicCamera camera = new OrthographicCamera(1200, 800);
    private InputCatcher inputCatcher;
    private Inventory inventory;
    private final static MessageAnimationStep MESSAGE_ANIMATION_STEP = new MessageAnimationStep();
    private final SpriteBatch batch = new SpriteBatch();
    private long lastTimeMoved;
    private final static long MINIMUM_TIME_TO_MOVE = 100L;

    @Getter private PointLight pointLight;
    private Body playerBody;

    public Corev2(Corev3 corev3) {
	super(corev3);
	networking = new Networking(this);
	init();
    }

    public void init() {
	try {
	    ItemsArchive.itemsArchive = new ItemsArchive(ItemsReader.read());
	} catch (Exception ex) {
	    Context.LOG.log(ex);
	}

	context = new GameContext(new CameraMan(), objects, this);
	context.addObject(new NetworkingObject(networking));
	context.checkSize();
	context.getCameraMan().setWindowWidth(800);
	context.getCameraMan().setWindowHeight(600);

	camera.setToOrtho(true);
	camera.update();
	stage.setCamera(camera);

	inputCatcher = new InputCatcher(this);
	inputCatcher.setBounds(0, 0, 800, 600);
	stage.addActor(inputCatcher);
	stage.setKeyboardFocus(inputCatcher);

	inventory = new Inventory(context);
	inventory.setBounds();
	stage.addActor(inventory);

	callToConnect();
	Thread thread = new Thread(this, "Corev2");
	thread.setPriority(Thread.MAX_PRIORITY);
	thread.start();
	thread = new Thread("Animation") {

	    @Override
	    public void run() {
		while (true) {
		    animate();
		    try {
			Thread.sleep(MessageAnimationStep.STEP);
		    } catch (InterruptedException ex) {
			Context.LOG.log(ex);
		    }
		}
	    }
	};
	thread.start();

	playerBody = GameContext.getWorld().createBody(new BodyDef());
	RayHandler rayHandler = GameContext.getRayHandler();
	pointLight = new PointLight(rayHandler, 64);
	pointLight.setColor(new Color(1, 1, 1, 0.05f));
	pointLight.setSoft(true);
	pointLight.setSoftnessLenght(2f);
	pointLight.setDistance(50);
	pointLight.setStaticLight(true);
    }

    private void callToConnect() {
	new Thread("Server starter") {
	    @Override
	    public void run() {
		try {
		    networking.connect(Main.IP, 54555);
		} catch (IOException ex) {
		    try {
			Main.main(new String[]{"-mode", "host", "-hostip", "null"});
		    } catch (ParseException ex1) {
			Context.LOG.log(ex1);
		    }
		    synchronized (Corev2.this) {
			try {
			    log(new LogString("Couldn't find a host on " + Main.IP, LogLevel.WARNING));
			    log(new LogString("Starting server on " + Main.IP, LogLevel.WARNING));
			    networking.connect(Main.IP, 54555);
			} catch (IOException ex1) {
			    Context.LOG.log(ex1);
			}
		    }
		}
	    }
	}.start();
    }

    private void animate() {
	context.sendAnimationStep();
	inventory.processMessage(MESSAGE_ANIMATION_STEP);
    }

    public void update(int delta) {
	if (notpaused) {
	    checkKeys();
	    MessageControlledByInput mc = checkMovementDesired();
	    int playerID = context.getPlayerID();
	    if (mc != null && playerID != -1) {
		context.sendDirectedMessage(new MessageToSend(mc));
	    }
	    for (AbstractGameObject gameObject : objects) {
		if (!notpaused) {
		    break;
		}
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

    private MessageControlledByInput checkMovementDesired() {
	MessageControlledByInput mc = null;
	boolean ableToMove = System.currentTimeMillis() - lastTimeMoved > MINIMUM_TIME_TO_MOVE
		&& !context.getPlayer().getPositionComponent().isAnimated() && !getConsole().hasFocus();
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
		case Input.Keys.R:
		    inventory.changeActiveHand();
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

    private World createPhysicsWorld() {

	World world = new World(new Vector2(0, -10), true);

	ChainShape chainShape = new ChainShape();
	chainShape.createLoop(new Vector2[]{new Vector2(-22, 1),
					    new Vector2(22, 1), new Vector2(22, 31), new Vector2(0, 20),
					    new Vector2(-22, 31)});
	BodyDef chainBodyDef = new BodyDef();
	chainBodyDef.type = BodyType.StaticBody;
	world.createBody(chainBodyDef).createFixture(chainShape, 0);
	chainShape.dispose();
	return world;
    }

    @Override
    public void draw() {
	context.getCameraMan().moveCamera();
	batch.setProjectionMatrix(context.getCameraMan().getCamera().combined);

	GameContext.getRayHandler().setCombinedMatrix(context.getCameraMan().getLightsCamera().combined);
	GameContext.getRayHandler().setCombinedMatrix(context.getCameraMan().getLightsCamera().combined);
	playerBody.setTransform(getContext().getPlayer().getPosition().getX(), getContext().getPlayer().getPosition().getY(), 0);
	pointLight.attachToBody(playerBody, 0.5f, 0.5f);

	batch.begin();
	context.getPaintables().forEach(paintableComponent -> paintableComponent.paint(batch));
	batch.end();
	GameContext.getRayHandler().updateAndRender();

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
	Context.LOG.log("Client has continued his work");
    }

    public void mouseClicked(int button, int x, int y) {
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
	    contextMenu.setActivationListener((UIElement e) -> {
		ContextMenu currentMenu = (ContextMenu) e;
		switch (currentMenu.getSelected()) {
		    case 0:
			String description = item.getProperties().getDescription();
			log(new LogString(description, LogLevel.TALKING));
			break;
		    case 1:
			int id = item.getId();
			int playerID = context.getPlayerID();
			MessagePropertySet messagePropertySet = new MessagePropertySet(playerID, "pull", id);
			MessageToSend messageToSend = new MessageToSend(messagePropertySet);
			context.sendDirectedMessage(messageToSend);
			break;
		}
		menu.hide();
		menu = null;
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
	context.setCameraToPlayer();
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
		Context.LOG.log(ex);
	    }
	}
    }
}
