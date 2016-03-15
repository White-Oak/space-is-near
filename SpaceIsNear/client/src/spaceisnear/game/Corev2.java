package spaceisnear.game;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import me.whiteoak.minlog.Log;
import org.apache.commons.lang3.ArrayUtils;
import spaceisnear.abstracts.AbstractGameObject;
import spaceisnear.game.components.client.PaintableComponent;
import spaceisnear.game.components.server.context.ServerContextMenu;
import spaceisnear.game.messages.*;
import spaceisnear.game.messages.properties.MessagePropertySet;
import spaceisnear.game.objects.items.StaticItem;
import spaceisnear.game.ui.Position;
import spaceisnear.game.ui.UIElement;
import spaceisnear.game.ui.console.ChatString;
import spaceisnear.game.ui.console.LogLevel;
import spaceisnear.game.ui.context.ContextMenu;
import spaceisnear.starting.ui.ScreenImprovedGreatly;

/**
 * @author LPzhelud
 */
@RequiredArgsConstructor public final class Corev2 extends ScreenImprovedGreatly {

    @Setter private int key;
    @Getter private boolean notpaused;

    @Getter private final Engine engine;
    @Getter private CameraMan cameraMan;

    private ContextMenu menu;
    private InputCatcher inputCatcher;
    private Inventory inventory;
    private final static MessageAnimationStep MESSAGE_ANIMATION_STEP = new MessageAnimationStep();
    private SpriteBatch batch;
    private long lastTimeMoved;
    private final static long MINIMUM_TIME_TO_MOVE = 100L;

    @Getter private PointLight pointLight;

    private final FPSLogger logger = new FPSLogger();
    //
    public static RayHandler rayHandler;
    public static World world = new World(new Vector2(), true);

    //
    @Getter private boolean created;

    @Override
    public void create() {
	batch = new SpriteBatch();
	//camera setup
	cameraMan = new CameraMan();
	cameraMan.setWindowWidth(800);
	cameraMan.setWindowHeight(600);
//	inputCatcher = new InputCatcher(this);
//	inputCatcher.setBounds(0, 0, 800, 600);
//	stage.addActor(inputCatcher);
//	stage.setKeyboardFocus(inputCatcher);

	inventory = new Inventory(engine);
	inventory.setBounds();
	stage.addActor(inventory);

//	Thread thread = new Thread(this, "Corev2");
//	thread.setPriority(Thread.MAX_PRIORITY);
//	thread.start();
	Thread thread = new Thread("Animation") {

	    @Override
	    public void run() {
		while (true) {
		    animate();
		    try {
			Thread.sleep(MessageAnimationStep.STEP);
		    } catch (InterruptedException ex) {
			Log.error("client", "While trying to sleep in Animation thread", ex);
		    }
		}
	    }
	};
	thread.start();

	pointLight = new PointLight(rayHandler, 64);
	pointLight.setColor(new Color(1, 1, 1, 0f));
	pointLight.setSoft(true);
	pointLight.setSoftnessLength(64f);
	pointLight.setDistance(500);
	pointLight.setStaticLight(true);
	pointLight.update();

	engine.addControllerToCore();

	created = true;
    }

    public void addContextMenu(ContextMenu conmenu) {
	if (menu == null) {
	    if (conmenu == null) {
		return;
	    }
	    menu = conmenu;
	    stage.addActor(menu);
	} else {
	    menu.hide();
	    menu = null;
	}
    }

    private void animate() {
	getContext().getAnimateds()
		.forEach(abstractGameObject -> abstractGameObject.message(MESSAGE_ANIMATION_STEP));
	cameraMan.animate();
	inventory.processMessage(MESSAGE_ANIMATION_STEP);
    }

    private GameContext getContext() {
	return engine.getContext();
    }

    @Override
    public void draw() {
	cameraMan.moveCamera(getCamera());
	batch.setProjectionMatrix(getCamera().combined);

	if (getContext().getPlayer() != null) {
	    rayHandler.setCombinedMatrix(getCamera());
	    pointLight.setPosition((getContext().getPlayer().getPosition().getX() + 0.5f) * GameContext.TILE_WIDTH,
		    (getContext().getPlayer().getPosition().getY() + 0.5f) * GameContext.TILE_HEIGHT);
	}
	batch.begin();
	final List<PaintableComponent> paintables = getContext().getPaintables();
	for (int i = 0; i < paintables.size(); i++) {
	    PaintableComponent get = paintables.get(i);
	    if (get.isOwnerDestroyed()) {
		paintables.remove(i);
	    } else {
		get.paint(batch);
	    }
	}
	batch.end();
	rayHandler.updateAndRender();

	cameraMan.unmoveCamera(getCamera());
	logger.log();
    }

    public void mouseClicked(int button, int x, int y) {
	int toAddX = cameraMan.getX();
	int toAddY = cameraMan.getY();
	int calculatedX = x / GameContext.TILE_WIDTH;
	int calculatedY = y / GameContext.TILE_HEIGHT;
	int tileX = toAddX + calculatedX;
	int tileY = toAddY + calculatedY;
	if (tileX < 0 || tileY < 0) {
	    return;
	}
	switch (button) {
	    case 1:
		if (menu == null) {
		    createContextMenuWithItems(x, y, tileX, tileY);
		} else {
		    menu.hide();
		    menu = null;
		}
		break;
	    case 0:
		final Position position = getContext().getPlayer().getPosition();
		int dx = Math.abs(position.getX() - tileX);
		int dy = Math.abs(position.getY() - tileY);
		if (dx <= 2 && dy <= 2) {
		    java.util.List<AbstractGameObject> itemsOn = getContext().itemsOn(tileX, tileY);
		    MessageInteracted messageInteracted;
		    int interactedWith = inventory.getItemInActiveHand().getItemId();
		    messageInteracted = new MessageInteracted(itemsOn.get(itemsOn.size() - 1).getId(), interactedWith);
		    MessageToSend messageToSend = new MessageToSend(messageInteracted);
		    getContext().sendDirectedMessage(messageToSend);
		}
		break;
	}
    }

    private void createContextMenuWithItems(int x, int y, int tileX, int tileY) {
	ContextMenu contextMenu = new ContextMenu(null, stage);
	contextMenu.setPosition(x, y);
	java.util.List<AbstractGameObject> itemsOn = getContext().itemsOn(tileX, tileY);
	ArrayList<Integer> ids = new ArrayList<>();
	itemsOn.stream()
		.map(staticItem -> (StaticItem) staticItem)
		.forEach(item -> {
		    addSubMenuFor(item, contextMenu);
		    ids.add(item.getId());
		});
	final int[] toPrimitive = ArrayUtils.toPrimitive(ids.toArray(new Integer[ids.size()]));
	//sending message to ask for set of actions
	MessageActionsRequest messageActionsRequest = new MessageActionsRequest(toPrimitive);
	MessageToSend messageToSend = new MessageToSend(messageActionsRequest);
	getContext().sendDirectedMessage(messageToSend);
	//Adding to CONTEXT
	menu = contextMenu;
	stage.addActor(menu);
//	testMenu(x, y);
    }

    public void updateCurrentMenu(ServerContextMenu update) {
	if (menu != null) {
	    menu.setItems(update.getLists(), update.getListeners(getContext()));
	}
    }

    public void menuWantsToHide() {
	menu.hide();
	menu = null;
    }

    private void addSubMenuFor(StaticItem item, ContextMenu contextMenu) {
	ContextMenu contextSubMenu = new ContextMenu(item.getProperties().getName(), stage);
	contextMenu.add(contextSubMenu);
	contextSubMenu.add("Learn");
	contextSubMenu.add("Pull");
	contextSubMenu.add("Take");
	contextMenu.setActivationListener((UIElement e) -> {
	    procDefaultContextActions(e, item);
	    menuWantsToHide();
	});
    }

    private void procDefaultContextActions(UIElement e, final StaticItem item) {
	ContextMenu currentMenu = (ContextMenu) e;
	switch (currentMenu.getSelected()) {
	    case 0:
		String description = item.getProperties().getDescription();
		getEngine().chat(new ChatString(description, LogLevel.TALKING));
		break;
	    case 1:
		int id = item.getId();
		int playerID = getContext().getPlayerID();
		MessagePropertySet messagePropertySet = new MessagePropertySet(playerID, "pull", id);
		MessageToSend messageToSend = new MessageToSend(messagePropertySet);
		getContext().sendDirectedMessage(messageToSend);
		break;
	}
    }
}
