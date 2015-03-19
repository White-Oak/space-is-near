package spaceisnear.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.*;
import lombok.Getter;
import lombok.Setter;
import spaceisnear.game.components.inventory.*;
import spaceisnear.game.messages.*;
import spaceisnear.game.messages.properties.MessageInventoryUpdated;
import spaceisnear.game.objects.Player;
import spaceisnear.game.objects.items.ItemsArchive;
import spaceisnear.game.objects.items.StaticItem;
import spaceisnear.game.ui.UIElement;
import spaceisnear.game.ui.console.LogLevel;
import spaceisnear.game.ui.console.ChatString;
import spaceisnear.game.ui.context.ContextMenu;

public class Inventory extends Actor {

    private final static int TILE_HEIGHT = 40, TILE_WIDTH = 40;
    private final static int TILE_PADDING = 5;
    public final static int INVENTORY_WIDTH = (TILE_WIDTH + TILE_PADDING) * 3;
    public final static int INVENTORY_HEIGHT = TILE_PADDING + (TILE_HEIGHT + TILE_PADDING) * 7;
    private int deltaX;
    private final static int DELTA_DELTA_X = 8;
    private final static int MAX_DELTA_X = (TILE_WIDTH + TILE_PADDING) * 2;
    @Getter @Setter private boolean minimized;
    private InventoryComponent inventoryComponent;
    private final GameContext context;
    private final static String[][] itemsPlacement = {{"mask", "head", "ear"},
						      {"costume", "body", "gloves"},
						      {"costume slot", "shoes", "belt"},
						      {"left pocket", "id", "right pocket"},
						      {"right hand"},
						      {"left hand"},
						      {"bag"}};
    private final static String[] itemsPlacementHidden = {"mask",
							  "body",
							  "belt",
							  "id",
							  "right hand",
							  "left hand",
							  "bag"};
    private int activeHand;

    private void drawTiles(int startingX, int startingY, ShapeRenderer renderer) {
	Color tileColor = new Color(0, 0, 0, 0.7f);
	//first two lines of tiles
	//hidden if animation
	renderer.setColor(tileColor);
	if (deltaX != MAX_DELTA_X) {
	    for (int i = 0; i < 2; i++) {
		int localDeltaX = deltaX;
		if (i == 1) {
		    localDeltaX >>= 1;
		}
		for (int j = 0; j < ACTIVE_HAND_START_Y; j++) {
		    renderer.rect(localDeltaX + startingX + i * (TILE_WIDTH + TILE_PADDING),
			    startingY + j * (TILE_HEIGHT + TILE_PADDING),
			    TILE_WIDTH, TILE_HEIGHT);
		}
	    }
	}
	//last line of tiles
	for (int i = 0; i < 7; i++) {
	    renderer.rect(startingX + (TILE_WIDTH + TILE_PADDING) * 2, startingY + i * (TILE_HEIGHT + TILE_PADDING),
		    TILE_WIDTH, TILE_HEIGHT);
	}
	renderer.end();
	//active hand representing
	renderer.begin(ShapeRenderer.ShapeType.Line);
	renderer.setColor(new Color(0, 1, 1, 1));
	renderer.rect(startingX + (TILE_WIDTH + TILE_PADDING) * 2 - 1,
		startingY + getActiveHandY() * (TILE_HEIGHT + TILE_PADDING) - 1,
		TILE_WIDTH + 2, TILE_HEIGHT + 2);
    }

    private InventoryComponent getInventoryComponent() {
	if (inventoryComponent == null) {
	    Player owner = context.getPlayer();
	    inventoryComponent = owner.getInventoryComponent();
	}
	return inventoryComponent;
    }

    private void drawItems(SpriteBatch batch, int startingX, int startingY) {
	if (getInventoryComponent() != null) {
	    batch.begin();
	    TypicalInventorySlotsSet slots = inventoryComponent.getSlots();
	    if (deltaX != MAX_DELTA_X) {
		for (int i = 0; i < itemsPlacement.length; i++) {
		    String[] strings = itemsPlacement[i];
		    for (int j = 0; j < strings.length; j++) {
			int localDeltaX = deltaX;
			if (j == 1) {
			    localDeltaX >>= 1;
			} else if (j == 0) {
			    localDeltaX = 0;
			}
			String string = strings[strings.length - 1 - j];
			int itemId = slots.get(string).getItemId();
			if (itemId > 0) {
			    StaticItem get = (StaticItem) inventoryComponent.getContext().getObjects().get(itemId);
			    int id = get.getProperties().getId();
			    int[] imageIds = ItemsArchive.itemsArchive.getImageIds(id);
			    TextureRegion textureRegion = ItemsArchive.itemsArchive.getTextureRegion(imageIds[0]);
			    final int length = strings.length == 1 ? 2 : (strings.length - 1 - j);
			    batch.draw(textureRegion,
				    startingX + localDeltaX + length * (TILE_WIDTH + TILE_PADDING) + (TILE_WIDTH - GameContext.TILE_WIDTH) / 2,
				    startingY + i * (TILE_HEIGHT + TILE_PADDING) + (TILE_HEIGHT - GameContext.TILE_HEIGHT) / 2);
			}
		    }
		}
	    } else {
		for (int i = 0; i < itemsPlacementHidden.length; i++) {
		    String string = itemsPlacementHidden[i];
		    int itemId = slots.get(string).getItemId();
		    if (itemId > 0) {
			StaticItem get = (StaticItem) inventoryComponent.getContext().getObjects().get(itemId);
			int id = get.getProperties().getId();
			int imageId = get.getProperties().getBundle().imageIds[0];
			TextureRegion textureRegion = ItemsArchive.itemsArchive.getTextureRegion(imageId);
			batch.draw(textureRegion, startingX + 2 * (TILE_WIDTH + TILE_PADDING),
				startingY + i * (TILE_HEIGHT + TILE_PADDING));
		    }
		}
	    }
	    batch.end();
	}
    }

    private void drawBackground(int startingX, int startingY, ShapeRenderer renderer) {
	Color backgroundColor = new Color(1, 1, 1, 0.5f);
	renderer.setColor(backgroundColor);
	renderer.rect(startingX - TILE_PADDING + deltaX, startingY - TILE_PADDING,
		MAX_DELTA_X - deltaX, TILE_PADDING + (TILE_HEIGHT + TILE_PADDING) * ACTIVE_HAND_START_Y);
	renderer.rect(startingX + (TILE_WIDTH + TILE_PADDING) * 2 - TILE_PADDING, startingY - TILE_PADDING,
		TILE_WIDTH + TILE_PADDING * 2, INVENTORY_HEIGHT);

    }

    public void processMessage(Message message) {
	switch (message.getMessageType()) {
	    case ANIMATION_STEP:
		if (minimized) {
		    deltaX += DELTA_DELTA_X;
		    if (deltaX > MAX_DELTA_X) {
			deltaX = MAX_DELTA_X;
		    }
		} else {
		    deltaX -= DELTA_DELTA_X;
		    if (deltaX < 0) {
			deltaX = 0;
		    }
		}
		break;
	}
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
	paintComponent(batch);
    }

    final ShapeRenderer renderer = new ShapeRenderer();
    final OrthographicCamera camera;

    public void paintComponent(SpriteBatch batch) {
	batch.end();
	batch.setProjectionMatrix(camera.combined);
	int x = (int) getX();
	int y = (int) getY();
	renderer.setProjectionMatrix(camera.combined);
	Gdx.gl.glEnable(GL20.GL_BLEND);
	Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	renderer.begin(ShapeRenderer.ShapeType.Filled);
	drawBackground(x, y, renderer);
	drawTiles(x, y, renderer);
	renderer.end();
	Gdx.gl.glDisable(GL20.GL_BLEND);
	drawItems(batch, x, y);
	batch.begin();
    }

    public Inventory(GameContext context) {
	this.context = context;
	camera = new OrthographicCamera();
	camera.setToOrtho(true);
	camera.update();
	addCaptureListener(new InputListener() {

	    @Override
	    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		Inventory.this.mouseClicked((int) x, (int) y, button);
		return true;
	    }
	});
    }

    public InventorySlot getItemInActiveHand() {
	return get(2, getActiveHandY());
    }

    private InventorySlot get(int x, int y) {
	return inventoryComponent.getSlots().get(getDefinition(x, y));
    }

    private StaticItem getItem(int x, int y) {
	final InventorySlot get = get(x, y);
	if (get == null || get.getItemId() < 0) {
	    return null;
	}
	return (StaticItem) context.getObjects().get(get.getItemId());
    }

    private String getDefinition(int x, int y) {
	if (y >= ACTIVE_HAND_START_Y) {
	    x -= 2;
	}
	if (x < 0) {
	    return null;
	}
	return minimized
		? (itemsPlacementHidden[y])
		: (itemsPlacement[y][x]);
    }

    private void moveActiveHandItemTo(int x, int y) {
	InventorySlot get = getItemInActiveHand();
	if (get.getItemId() > 0) {
	    InventorySlot newOne = get.getWithNewName(getDefinition(x, y));
	    sendInventoryUpdate(newOne, get);
	}
    }

    private void sendInventoryUpdate(InventorySlot newOne, InventorySlot get) {
	InventoryComponent.Update update = new InventoryComponent.MovedUpdate(newOne, get);
	MessageInventoryUpdated messageInventoryUpdated;
	messageInventoryUpdated = new MessageInventoryUpdated(inventoryComponent.getOwnerId(), update);
	context.sendDirectedMessage(new MessageToSend(messageInventoryUpdated));
    }

    private void moveToActiveHandFrom(int x, int y) {
	InventorySlot get = get(x, y);
	if (get.getItemId() > 0) {
	    InventorySlot newOne = get.getWithNewName(getDefinition(2, getActiveHandY()));
	    sendInventoryUpdate(newOne, get);
	}
    }

    private void interactWithItemInActiveHand(int x, int y) {
	InventorySlot get = get(x, y);
	if (get.getItemId() > 0) {
	    MessageInteracted messageInteraction = new MessageInteracted(get.getItemId(), getItemInActiveHand().getItemId());
	    context.sendDirectedMessage(new MessageToSend(messageInteraction));
	}
    }

    private int getActiveHandY() {
	return ACTIVE_HAND_START_Y + activeHand;
    }
    private static final int ACTIVE_HAND_START_Y = 4;

    public void mouseClicked(int x, int y, int button) {
	int tileX = x / (TILE_WIDTH + TILE_PADDING);
	int tileY = y / (TILE_HEIGHT + TILE_PADDING);
	//System.out.println(String.format("You reached that at %s %s", tileX, tileY));
	switch (button) {
	    case 0:
		if (tileY != getActiveHandY()) {
		    if (getItemInActiveHand().getItemId() > 0) {
			if (get(tileX, tileY).getItemId() < 0) {
			    moveActiveHandItemTo(tileX, tileY);
			} else {
			    interactWithItemInActiveHand(tileX, tileY);
			}
		    } else {
			if (get(tileX, tileY).getItemId() > 0) {
			    moveToActiveHandFrom(tileX, tileY);
			}
		    }
		}
		break;
	    case 1:
		createContextMenuWithItem(x, y, getItem(tileX, tileY));
		break;
	}
    }

    public void setBounds() {
	super.setBounds(800 - INVENTORY_WIDTH, TILE_PADDING, INVENTORY_WIDTH, INVENTORY_HEIGHT);
    }

    public void changeActiveHand() {
	activeHand++;
	activeHand %= 2;
    }

    private void createContextMenuWithItem(int x, int y, StaticItem item) {
	if (item == null) {
	    context.getCore().addContextMenu(null);
	    return;
	}
	ContextMenu contextMenu = new ContextMenu(null, context.getCore().getStage());
	contextMenu.setPosition(getX() + x, getY() + y);
	addSubMenuFor(item, contextMenu);
	context.getCore().addContextMenu(contextMenu);
    }

    private void addSubMenuFor(StaticItem item, ContextMenu contextMenu) {
	ContextMenu contextSubMenu = new ContextMenu(item.getProperties().getName(), context.getCore().getStage());
	contextMenu.add(contextSubMenu);
	contextSubMenu.add("Learn");
	contextMenu.setActivationListener((UIElement e) -> {
	    procDefaultContextActions(e, item);
	    context.getCore().addContextMenu(null);
	});
    }

    private void procDefaultContextActions(UIElement e, final StaticItem item) {
	ContextMenu currentMenu = (ContextMenu) e;
	switch (currentMenu.getSelected()) {
	    case 0:
		String description = item.getProperties().getDescription();
		context.getCore().chat(new ChatString(description, LogLevel.TALKING));
		break;
	}
    }
}
