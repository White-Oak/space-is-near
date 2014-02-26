package spaceisnear.server;

import java.util.*;
import lombok.Getter;
import spaceisnear.abstracts.AbstractGameObject;
import spaceisnear.abstracts.Context;
import spaceisnear.game.GameContext;
import spaceisnear.game.components.inventory.InventorySlot;
import spaceisnear.game.components.server.VariablePropertiesComponent;
import spaceisnear.game.layer.AtmosphericLayer;
import spaceisnear.game.layer.ObstaclesLayer;
import spaceisnear.game.messages.*;
import spaceisnear.game.objects.GameObjectType;
import spaceisnear.game.objects.Position;
import spaceisnear.game.ui.console.LogString;
import spaceisnear.server.objects.*;
import spaceisnear.server.objects.items.ServerItemsArchive;
import spaceisnear.server.objects.items.StaticItem;

/**
 * @author LPzhelud
 */
public final class ServerContext extends Context {

    public static final int TILE_HEIGHT = 16;
    public static final int TILE_WIDTH = 16;
    @Getter private final ServerNetworking networking;
    @Getter private final List<AbstractGameObject> objects;
    @Getter private final List<Player> players = new LinkedList<>();
    @Getter private final ObstaclesLayer obstacles;
    @Getter private final AtmosphericLayer atmosphere;
    private final ServerLog log = new ServerLog();
    private static final int MAXIMUM_TILES_TO_BE_HEARD = 20;
    public static final int HIDDEN_SERVER_OBJECTS = 1;

    @Override
    public synchronized void sendThemAll(Message m) {
	if (!(m instanceof DirectedMessage)) {
	    for (AbstractGameObject gameObject : objects) {
		gameObject.message(m);
	    }
	}
    }

    @Override
    public synchronized void sendToID(Message m, int id) {
	objects.get(id).message(m);
    }

    public synchronized void addObject(ServerGameObject gameObject) {
	objects.add(gameObject);
	if (gameObject != null) {
	    gameObject.setId(objects.size() - 1);
	}
    }

    public synchronized Player addPlayer(int connectionID) {
	Player player = new Player(this, connectionID);
	players.add(player);
	addObject(player);
	return player;
    }

    public Player getPlayer(int id) {
	return (Player) players.get(id);
    }

    public ServerContext(final ServerNetworking networking, final List<AbstractGameObject> objects,
			 ObstaclesLayer obstacles,
			 AtmosphericLayer atmosphere) {
	this.networking = networking;
	this.objects = objects;
	this.obstacles = obstacles;
	this.atmosphere = atmosphere;
	addObject(new ServerNetworkingObject(this));
	checkSize();
    }

    public void checkSize() {
	while (objects.size() < GameContext.HIDDEN_CLIENT_OBJECTS) {
	    addObject(null);
	}
    }

    public void logToServerLog(LogString string) {
	log.log(string);
    }

    public boolean isHearingLogMessage(Position said, Position toHear) {
	int[][] bufferMap = new int[obstacles.getWidth()][obstacles.getHeight()];
	int initialX = said.getX();
	int initialY = said.getY();
	//how to name it???
	START_RECURSION(bufferMap, initialX, initialY, MAXIMUM_TILES_TO_BE_HEARD);
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
	for (AbstractGameObject abstractGameObject : objects) {
	    if (abstractGameObject.getType() == GameObjectType.ITEM) {
		StaticItem item = (StaticItem) abstractGameObject;
		if (item.getProperties().getId() == ServerItemsArchive.RADIO_ID) {
		    String property = (String) item.getVariableProperties().getProperty("frequency");
		    if (property.equals(frequency)) {
			boolean enabled = (boolean) item.getVariableProperties().getProperty("enabled");
			if (enabled) {
			    Position position = item.getPosition();
			    START_RECURSION(bufferMap, position.getX() - 1, position.getY(), MAXIMUM_TILES_TO_BE_HEARD - 1);
			    START_RECURSION(bufferMap, position.getX() + 1, position.getY(), MAXIMUM_TILES_TO_BE_HEARD - 1);
			    START_RECURSION(bufferMap, position.getX(), position.getY() - 1, MAXIMUM_TILES_TO_BE_HEARD - 1);
			    START_RECURSION(bufferMap, position.getX(), position.getY() + 1, MAXIMUM_TILES_TO_BE_HEARD - 1);
			}
		    }
		}
	    }
	}
	return bufferMap;
    }

    public boolean isOnMap(int x, int y) {
	return (x >= 0 && x < obstacles.getWidth()) && (y >= 0 && y < obstacles.getHeight());
    }

    public void log(final LogString log) {
	logToServerLog(log);
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
			getNetworking().sendToID(i, new MessageLog(log));
		    }
		}
		break;
	    case OOC:
		getNetworking().sendToAll(new MessageLog(log));
		break;
	}
    }

    private void processIncomingTalkingLogMessage(final LogString log) {
	for (int i = 0; i < players.size(); i++) {
	    Player player = players.get(i);
	    Position positionToHear = player.getPosition();
	    Position positionToSay = log.getPosition();
	    if (isHearingLogMessage(positionToSay, positionToHear)) {
		getNetworking().sendToID(i, new MessageLog(log));
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
		if (frequency.equals(variableProperties.getProperty("frequency"))) {
		    radioPlayer = (boolean) variableProperties.getProperty("enabled");
		}
	    }
	}
	return radioPlayer;
    }

}
