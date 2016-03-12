package spaceisnear.server;

import me.whiteoak.minlog.Log;
import java.util.*;
import lombok.Getter;
import spaceisnear.abstracts.AbstractGameObject;
import spaceisnear.abstracts.Context;
import spaceisnear.game.components.inventory.InventorySlot;
import spaceisnear.game.components.VariablePropertiesComponent;
import spaceisnear.game.layer.AtmosphericLayer;
import spaceisnear.game.layer.ObstaclesLayer;
import spaceisnear.game.messages.*;
import spaceisnear.game.messages.properties.MessagePropertySet;
import spaceisnear.game.objects.GameObjectType;
import spaceisnear.game.ui.Position;
import spaceisnear.game.ui.console.ChatString;
import spaceisnear.server.objects.*;
import spaceisnear.server.objects.items.ServerItemsArchive;
import spaceisnear.server.objects.items.StaticItem;
import spaceisnear.server.scriptsv2.ContextProcessorScript;
import spaceisnear.server.scriptsv2.ScriptsManager;

/**
 * @author LPzhelud
 */
public final class ServerContext extends Context {

    public static final int TILE_HEIGHT = 16;
    public static final int TILE_WIDTH = 16;
    @Getter private final ServerNetworking networking;
    @Getter private final List<Player> players = new LinkedList<>();
    @Getter private final ObstaclesLayer obstacles;
    @Getter private final AtmosphericLayer atmosphere;
    private static final int MAXIMUM_TILES_TO_BE_HEARD = 20, MAXIMUM_TILES_TO_BE_WHISPERED = 2;
    public static final int HIDDEN_SERVER_OBJECTS = 1;
    private final List<AbstractGameObject> timeNeeding = new ArrayList<>();
    @Getter private final ScriptsManager scriptsManager;

    @Override
    public synchronized void sendThemAll(Message m) {
	if (!(m instanceof DirectedMessage)) {
	    objects.values().forEach(gameObject -> gameObject.message(m));
	}
    }

    public void sendTimePassed(MessageTimePassed mtp) {
	timeNeeding.forEach(abstractGameObject -> abstractGameObject.message(mtp));
    }

    @Override
    public synchronized void sendToID(Message m, int id) {
	objects.get(id).message(m);
    }

    public synchronized void addObject(ServerGameObject gameObject, int id) {
	if (gameObject != null) {
	    objects.put(id, gameObject);
	}
    }

    public synchronized void addObject(ServerGameObject gameObject) {
	if (gameObject != null) {
	    gameObject.setId(objects.size() - 1);
	    objects.put(gameObject.getId(), gameObject);
	}
    }

    public synchronized Player addPlayer() {
	Player player = new Player(this);
	players.add(player);
	addObject(player);
	return player;
    }

    public Player getPlayer(int id) {
	return (Player) players.get(id);
    }

    public ServerContext(final ServerNetworking networking, final HashMap<Integer, AbstractGameObject> objects,
			 ObstaclesLayer obstacles,
			 AtmosphericLayer atmosphere) {
	super(objects);
	this.scriptsManager = new ScriptsManager();
	this.networking = networking;
	this.obstacles = obstacles;
	this.atmosphere = atmosphere;
	addObject(new ServerNetworkingObject(this), Context.NETWORKING_ID);
    }

    public boolean isHearingLogMessage(Position said, Position toHear) {
	int[][] bufferMap = new int[obstacles.getWidth()][obstacles.getHeight()];
	int initialX = said.getX();
	int initialY = said.getY();
	//how to name it???
	START_RECURSION(bufferMap, initialX, initialY, MAXIMUM_TILES_TO_BE_HEARD);
	return bufferMap[toHear.getX()][toHear.getY()] > 0;
    }

    public boolean isHearingWhispering(Position said, Position toHear) {
	int[][] bufferMap = new int[obstacles.getWidth()][obstacles.getHeight()];
	int initialX = said.getX();
	int initialY = said.getY();
	//how to name it???
	START_RECURSION(bufferMap, initialX, initialY, MAXIMUM_TILES_TO_BE_WHISPERED);
	return bufferMap[toHear.getX()][toHear.getY()] > 0;
    }

    private void START_RECURSION(int[][] bufferMap, int x, int y, int distance) {
	if (atmosphere.getPressure(x, y) < 5) {
	    return;
	}
	bufferMap[x][y] = distance;
	distance--;
	if (distance == 0) {
	    return;
	}
	try {
	    if (bufferMap[x - 1][y] < distance) {
		START_RECURSION(bufferMap, x - 1, y, distance);
	    }
	} catch (ArrayIndexOutOfBoundsException e) {
	}
	try {
	    if (bufferMap[x][y - 1] < distance) {
		START_RECURSION(bufferMap, x, y - 1, distance);
	    }
	} catch (ArrayIndexOutOfBoundsException e) {
	}
	try {
	    if (bufferMap[x][y + 1] < distance) {
		START_RECURSION(bufferMap, x, y + 1, distance);
	    }
	} catch (ArrayIndexOutOfBoundsException e) {
	}
	try {
	    if (bufferMap[x + 1][y] < distance) {
		START_RECURSION(bufferMap, x + 1, y, distance);
	    }
	} catch (ArrayIndexOutOfBoundsException e) {
	}
    }

    public int[][] getAvailabilityMatrixOfHearingFrequency(String frequency) {
	int[][] bufferMap = new int[obstacles.getWidth()][obstacles.getHeight()];
	objects.values()
		.stream()
		.filter(abstractGameObject -> abstractGameObject.getType() == GameObjectType.ITEM)
		.map(abstractGameObject -> (StaticItem) abstractGameObject)
		.filter(item -> item.getProperties().getId() == ServerItemsArchive.ITEMS_ARCHIVE.getIdByName("radio"))
		.filter(item -> item.getVariableProperties().getProperty("frequency").equals(frequency))
		.filter(item -> (boolean) item.getVariableProperties().getProperty("enabled"))
		.map(item -> item.getPosition())
		.forEach(position -> {
		    START_RECURSION(bufferMap, position.getX() - 1, position.getY(), MAXIMUM_TILES_TO_BE_HEARD - 1);
		    START_RECURSION(bufferMap, position.getX() + 1, position.getY(), MAXIMUM_TILES_TO_BE_HEARD - 1);
		    START_RECURSION(bufferMap, position.getX(), position.getY() - 1, MAXIMUM_TILES_TO_BE_HEARD - 1);
		    START_RECURSION(bufferMap, position.getX(), position.getY() + 1, MAXIMUM_TILES_TO_BE_HEARD - 1);
		});
	return bufferMap;
    }

    public boolean isOnMap(int x, int y) {
	return (x >= 0 && x < obstacles.getWidth()) && (y >= 0 && y < obstacles.getHeight());
    }

    public void chatLog(final ChatString log) {
	Log.info("chat", "[" + log.getLevel() + "] " + log.toString());
	switch (log.getLevel()) {
	    case TALKING:
		processIncomingTalkingLogMessage(log);
		break;
	    case BROADCASTING:
		int[][] bufferMap = getAvailabilityMatrixOfHearingFrequency(log.getFrequency());
		for (int i = 0; i < players.size(); i++) {
		    Player player = players.get(i);
		    boolean radioPlayer = doesPlayerHasEnabledRadioOnFrequency(player, log.getFrequency());
		    if (radioPlayer || bufferMap[player.getPosition().getX()][player.getPosition().getY()] > 0) {
			getNetworking().sendToClientID(i, new MessageChat(log));
		    }
		}
		break;
	    case OOC:
		getNetworking().sendToAll(new MessageChat(log));
		break;
	    case WHISPERING:
		processIncomingWhisperingLogMessage(log);
		break;
	    case PRIVATE:
		final AbstractGameObject get1 = getObjects().get(log.getReceiverID());
		if (get1.getType() == GameObjectType.PLAYER) {
		    Player get = (Player) get1;
		    get.message(new MessagePropertySet(log.getReceiverID(), "messages", log));
		}
		break;
	}
    }

    private void processIncomingTalkingLogMessage(final ChatString log) {
	for (int i = 0; i < players.size(); i++) {
	    Player player = players.get(i);
	    Position positionToHear = player.getPosition();
	    Position positionToSay = log.getPosition();
	    if (isHearingLogMessage(positionToSay, positionToHear)) {
		getNetworking().sendToClientID(i, new MessageChat(log));
	    }
	}
    }

    private void processIncomingWhisperingLogMessage(final ChatString log) {
	for (int i = 0; i < players.size(); i++) {
	    Player player = players.get(i);
	    Position positionToHear = player.getPosition();
	    Position positionToSay = log.getPosition();
	    if (isHearingLogMessage(positionToSay, positionToHear)) {
		getNetworking().sendToClientID(i, new MessageChat(log));
	    }
	}
    }

    private boolean doesPlayerHasEnabledRadioOnFrequency(Player player, String frequency) {
	InventorySlot ear = player.getInventoryComponent().getSlots().get("ear");
	boolean radioPlayer = false;
	final int itemId = ear.getItemId();
	if (itemId != -1) {
	    StaticItem get = (StaticItem) getObjects().get(itemId);
	    String name = get.getProperties().getName();
	    if (name.equals("ear_radio")) {
		VariablePropertiesComponent variableProperties = get.getVariableProperties();
		Object property = variableProperties.getProperty("frequency");
		if (property != null) {
		    if (frequency.equals(property)) {
			property = variableProperties.getProperty("enabled");
			if (property != null) {
			    radioPlayer = (boolean) property;
			}
		    }
		}
	    }
	}
	return radioPlayer;
    }

    public boolean addTimeNeeding(AbstractGameObject e) {
	return timeNeeding.add(e);
    }

    public boolean removeTimeNeeding(AbstractGameObject e) {
	return timeNeeding.remove(e);
    }

    public void proccessActionInContext(Client client, int itemId, int chosen) {
	StaticItem item = (StaticItem) objects.get(itemId);
	ContextProcessorScript processorScript = (ContextProcessorScript) scriptsManager
		.getScriptFor(ScriptsManager.ScriptType.CONPROC, item.getProperties().getName());
	if (processorScript != null) {
	    processorScript.init(this, item, chosen, client.getPlayer().getId());
	    processorScript.script();
	}
    }
}
