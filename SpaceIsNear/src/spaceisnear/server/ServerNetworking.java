package spaceisnear.server;

import com.esotericsoftware.kryonet.*;
import com.esotericsoftware.minlog.Logs;
import java.io.*;
import java.util.*;
import lombok.*;
import spaceisnear.abstracts.AbstractGameObject;
import spaceisnear.game.messages.*;
import spaceisnear.game.messages.properties.*;
import spaceisnear.game.messages.service.*;
import spaceisnear.game.messages.service.onceused.*;
import spaceisnear.game.objects.*;
import spaceisnear.game.ui.console.ChatString;
import spaceisnear.game.ui.console.LogLevel;
import spaceisnear.server.Client;
import spaceisnear.server.objects.Player;
import spaceisnear.server.objects.items.*;

/**
 * @author white_oak
 */
@RequiredArgsConstructor public class ServerNetworking extends Listener {

    public Server server;
    private final ServerCore core;

    private final List<Client> clients = new ArrayList<>();

    private final Queue<Message> messages = new LinkedList<>();
    private final Queue<Connection> connectionsForMessages = new LinkedList<>();

    private final AccountManager accountManager = new AccountManager();

    //
    private final List<Client> pendingRogers = new ArrayList<>();

    @Override
    public void received(Connection connection, Object object) {
	if (object instanceof Message) {
	    Message message = (Message) object;
	    switch (message.getMessageType()) {
		case ROGERED:
		    getClientByConnection(connection).setRogered(true);
		    break;
		default:
		    connectionsForMessages.add(connection);
		    messages.add(message);
	    }
	} else {
	    if (object instanceof String) {
		Logs.info("server", "Got string over the net: " + object);
	    }
	}
    }

    public void processReceivedQueue() {
	while (!messages.isEmpty()) {
	    processBundle(messages.poll(), connectionsForMessages.poll());
	}
    }

    private int getClientIDByConnection(Connection connection) {
	for (int i = 0; i < clients.size(); i++) {
	    Client client = clients.get(i);
	    if (client.getConnection() == connection) {
		return i;
	    }
	}
	return -1;
    }

    private Client getClientByConnection(Connection connection) {
	final int clientIDByConnection = getClientIDByConnection(connection);
	return clientIDByConnection >= 0 ? clients.get(clientIDByConnection) : null;
    }

    private void processBundle(Message message, Connection connection) {
	MessageType mt = message.getMessageType();
	final Client clientByConnection = getClientByConnection(connection);
	switch (mt) {
	    case PLAYER_INFO:
		processPlayerInformation(message, connection);
		break;
	    case LOGIN_INFO:
		processLoginInformation(message, clientByConnection, connection);
		break;
	    default:
		message.processForServer(core.getContext(), clientByConnection);
		break;
	}
    }

    private void processLoginInformation(Message message, final Client clientByConnection, Connection connection) {
	MessageLogin mci = (MessageLogin) message;
	clientByConnection.setClientInformation(mci);
	String messageS = mci.getLogin() + " requested access with password " + mci.getPassword();
	boolean accessible = accountManager.isAccessible(mci.getLogin(), mci.getPassword());
	messageS += accessible ? " and successfully got it." : " but does not seem to provide legal data.";
	Logs.info("server", messageS);
	sendToConnection(connection, new MessageAccess(accessible));
	if (accessible) {
	    seeIfOldClient(clientByConnection, mci, connection);
	    Logs.info("server", "Client information received");
	}
    }

    private synchronized void seeIfOldClient(final Client clientByConnection, MessageLogin mci, Connection connection) {
	clients.stream()
		.filter(client -> clientByConnection != client)//newly created client is still in list
		.filter(client -> client.getClientInformation().getLogin().equals(mci.getLogin()))
		.forEach(client -> {
		    client.setConnection(connection);
		    clients.remove(clientByConnection);
		    sendToConnection(connection, new MessageJoined());
		    Runnable runnable = () -> processOldPlayer(client);
		    new Thread(runnable, "sending world to old player").start();
		});
//	for (Client client : clients) {
//	    //newly created client is still in list
//	    if (clientByConnection != client) {
//		MessageLogin clientInformation = client.getClientInformation();
//		if (clientInformation.getLogin().equals(mci.getLogin())) {
//		    client.setConnection(connection);
//		    clients.remove(clientByConnection);
//		    sendToConnection(connection, new MessageJoined());
//		    Runnable runnable = () -> processOldPlayer(client);
//		    new Thread(runnable, "sending world to old player").start();
//		    break;
//		}
//	    }
//	}
    }

    private void processPlayerInformation(Message message, Connection connection) {
	MessagePlayerInformation mpi = (MessagePlayerInformation) message;
	final Client client = getClientByConnection(connection);
	client.setPlayerInformation(mpi);
	Logs.info("Server", "Player information received");
	sendToConnection(connection, new MessageJoined());
	connectedWantsPlayer(client);
    }

    public void sendToAll(NetworkableMessage message) {
	server.sendToAllTCP(message);
    }

    public void sendToConnection(Connection connection, NetworkableMessage message) {
	if (messagesSent == MESSAGES_TO_SEND_BEFORE_REQUESTING_ROGERING) {
	    server.sendToTCP(connection.getID(), new MessageRogerRequested());
	    waitForToRoger(connection);
	    messagesSent = 0;
	}
	server.sendToTCP(connection.getID(), message);
	messagesSent++;
    }

    public void sendToConnectionID(int id, NetworkableMessage message) {
	Connection connection = null;
	for (Client client : clients) {
	    if (client.getConnection().getID() == id) {
		connection = client.getConnection();
	    }
	}
	if (connection == null) {
	    return;
	}
	sendToConnection(connection, message);
    }

    public void sendToClientID(int id, NetworkableMessage message) {
	sendToConnection(clients.get(id).getConnection(), message);
    }

    public void sendToPlayer(Player player, NetworkableMessage message) {
	for (int i = 0; i < clients.size(); i++) {
	    Client client = clients.get(i);
	    if (client.getPlayer() == player) {
		sendToClientID(i, message);
	    }
	    break;
	}
    }

    private int messagesSent;
    private final int MESSAGES_TO_SEND_BEFORE_REQUESTING_ROGERING = 255;

    @Override
    public void disconnected(Connection connection) {//Possibly should rewrite this shit
	synchronized (clients) {
	    Client clientByConnection = getClientByConnection(connection);
	    MessageLogin clientInformation = clientByConnection.getClientInformation();
	    if (clientInformation != null) {
		accountManager.disconnect(clientInformation.getLogin());
	    }
	    clientByConnection.setConnection(null);
	}
    }

    @Override
    public synchronized void connected(Connection connection) {
	//Possibly should rewrite this shit
	synchronized (clients) {
	    Client client = new Client(connection);
	    clients.add(client);
	    //make some kind of queue or something like that to prevent blocking
	    if (core.isPaused()) {
		sendToConnection(connection, new MessageConnectionBroken());
		connection.close();
	    }
	}
    }

    private synchronized void connectedWantsPlayer(final Client client) {
	Runnable runnable = () -> processNewPlayer(client);
	new Thread(runnable, "New player creating").start();
    }

    private List<ObjectMessaged> createPlayer(Client client) {
	List<ObjectMessaged> list = new LinkedList<>();

	Player player = core.addPlayer();
	client.setPlayer(player);
	player.setNickname(client.getPlayerInformation().getDesiredNickname());
	player.getPosition().setX(1);
	player.getPosition().setY(1);
	list.add(getObjectMessaged(player));

	StaticItem ear = addToPlayerItem("ear_radio", "ear", player);
	list.add(getObjectMessaged(ear));

	StaticItem right_pocket = addToPlayerItem("pda", "right pocket", player);
	list.add(getObjectMessaged(right_pocket));

	StaticItem id = addToPlayerItem("id", "id", player);

	id.getVariableProperties().setProperty("name", client.getPlayerInformation().getDesiredNickname());
	id.getVariableProperties().setProperty("profession", client.getPlayerInformation().getDesiredProfession());
	list.add(getObjectMessaged(id));
	return list;
    }

    private StaticItem addToPlayerItem(String itemName, String nameOfInventorySlot, Player player) {
	final ServerContext context = core.getContext();
	final ServerItemsArchive itemsArchive = ServerItemsArchive.ITEMS_ARCHIVE;
	int idByName = itemsArchive.getIdByName(itemName);
	StaticItem item = ItemAdder.addItem(new Position(-1, -1), idByName, context, null);
	item.setPlayerId(player.getId());
	player.getInventoryComponent().getSlots().get(nameOfInventorySlot).setItemId(item.getId());
	return item;
    }

    public final static int BUFFER_SIZE = 256 * 512, O_BUFFER_SIZE = 512;

    public void host() throws IOException {
	server = new Server(BUFFER_SIZE, O_BUFFER_SIZE);
	Registerer.registerEverything(server);
	server.start();
	server.addListener(this);
	server.bind(54555);
    }

    private void processOldPlayer(Client client) {
	sendToAll(new MessagePaused());
	Logs.info("server", "Server's been paused");
	ObjectMessaged[] world = getWorld();
	sendWorldInformation(world, client);
	sendCreatedsOfWorld(world, client.getConnection());
	orderEveryoneToRogerAndWait();
	sendPlayer(client);
	sendToAll(new MessageUnpaused());
	sendPropertiesOfWorld(world, client);
	Logs.info("server", "Server has continued his work");
    }

    private void processNewPlayer(final Client client) {
//	waitForServerToStop();
	//
	sendToAll(new MessagePaused());

	List<ObjectMessaged> objectPlayer = createPlayer(client);
	ObjectMessaged[] world = getWorld();
	sendWorldInformation(world, client);
	sendCreatedsOfWorld(world, client.getConnection());
	sendPlayer(client);
	//rework
	clients.stream()
		.filter(client1 -> client1 != client)
		.forEach(client1 -> {
		    objectPlayer.forEach(objectMessaged -> {
			sendToConnection(client1.getConnection(), objectMessaged.created);
			sendProperties(objectMessaged.propertables, client1.getConnection());
		    });
		});
	//	

	sendToAll(new MessageUnpaused());
	sendPropertiesOfWorld(world, client);
	//@working fix that
	Runnable runnable = () -> {
	    for (int i = 0; i < 15; i++) {
		waitSomeTime();
	    }
	    Player get = client.getPlayer();
	    String message = get.getNickname() + " has connected to SIN!";
	    core.getContext().chatLog(new ChatString(message, LogLevel.BROADCASTING, "145.9"));
	    core.getContext().chatLog(new ChatString("Nanotracen welcomes you, " + get.getNickname() + ", and wishes best luck.",
		    LogLevel.BROADCASTING,
		    "145.9"));
	};
	new Thread(runnable, "Messaging about connected").start();
    }

    private void orderEveryoneToRogerAndWait() {
	if (!pendingRogers.isEmpty()) {
	    server.close();
	    throw new ConcurrentModificationException("Someone already waited for rogers when another one attempted to wait once again");
	}
	synchronized (clients) {
	    clients.stream()
		    .filter(client -> client.getConnection() != null && client.getClientInformation() != null)
		    .forEach(client -> pendingRogers.add(client));
	}
	server.sendToAllTCP(new MessageRogerRequested());
	waitForAllToRoger();
    }

    private void sortPropertiesByClosestToPlayer(List<MessagePropertable> propertables, Player player) {
	final Position playerPosition = player.getPosition();
	propertables.sort((MessagePropertable o1, MessagePropertable o2) -> {
	    int id = o1.getId();
	    AbstractGameObject ago = core.getContext().getObjects().get(id);
	    Position position1 = ago.getPosition();
	    id = o2.getId();
	    ago = core.getContext().getObjects().get(id);
	    Position position2 = ago.getPosition();
	    int distanceTo1 = playerPosition.distanceTo(position1);
	    int distanceTo2 = playerPosition.distanceTo(position2);
	    return Integer.compare(distanceTo1, distanceTo2);
	});
    }

    private void sendWorldInformation(ObjectMessaged[] world, Client client) {
	MessageWorldInformation mwi = getWorldInformation(world);
	sendToConnection(client.getConnection(), mwi);
    }

    private MessageWorldInformation getWorldInformation(ObjectMessaged[] world) {
	int accumulator = 0;
	for (ObjectMessaged objectMessaged : world) {
	    accumulator += objectMessaged.propertables.size();
	}
	MessageWorldInformation mwi = new MessageWorldInformation(world.length, accumulator);
	return mwi;
    }

    private void sendCreatedsOfWorld(ObjectMessaged[] world, Connection connection) {
	MessageCloned cloned = null;
	for (int i = 0; i < world.length; i++) {
	    ObjectMessaged objectMessaged = world[i];
	    MessageCreated messageCreated = objectMessaged.created;
	    if (i > 0 && messageCreated instanceof MessageCreatedItem && world[i - 1].created instanceof MessageCreatedItem) {
		MessageCreatedItem mci = (MessageCreatedItem) messageCreated;
		MessageCreatedItem mcilast = (MessageCreatedItem) world[i - 1].created;
		if (mci.getId() == mcilast.getId()) {
		    int amount = cloned == null ? 1 : cloned.amount + 1;
		    cloned = new MessageCloned();
		    cloned.amount = amount;
		    continue;
		} else {
		    if (cloned != null) {
			sendToConnection(connection, cloned);
			cloned = null;
		    }
		}
	    } else {
		if (cloned != null) {
		    sendToConnection(connection, cloned);
		    cloned = null;
		}
	    }
	    sendToConnection(connection, messageCreated);
	}
	if (cloned != null) {
	    sendToConnection(connection, cloned);
	}
    }

    private void sendPropertiesOfWorld(ObjectMessaged[] world, Client client) {
	//properties
	List<MessagePropertable> propertables = new ArrayList<>();
	for (ObjectMessaged objectMessaged : world) {
	    List<MessagePropertable> propertable = objectMessaged.propertables;
	    propertables.addAll(propertable);
	}
	sortPropertiesByClosestToPlayer(propertables, client.getPlayer());
	sendProperties(propertables, client.getConnection());
    }

    private void sendProperties(List<MessagePropertable> propertable, Connection connection) {
	propertable.forEach(messagePropertable -> sendToConnection(connection, messagePropertable));
    }

    private void sendPlayer(Client client) {
	MessageYourPlayerDiscovered mypd = new MessageYourPlayerDiscovered(client.getPlayer().getId());
	sendToConnection(client.getConnection(), mypd);
	Logs.info("server", "Player has sent");
    }

    private ObjectMessaged[] getWorld() {
	ServerContext context = core.getContext();
	List<AbstractGameObject> objects = context.getObjects();
	List<ObjectMessaged> messagesToReturn = new ArrayList<>();
	objects.stream()
		.map(object -> getObjectMessaged(object))
		.filter(objectMessaged -> objectMessaged != null)
		.forEach(objectMessaged -> messagesToReturn.add(objectMessaged));
	return messagesToReturn.toArray(new ObjectMessaged[messagesToReturn.size()]);
    }

    private ObjectMessaged getObjectMessaged(AbstractGameObject object) {
	ObjectMessaged objectMessaged = null;
	if (object != null && object.getId() >= ServerContext.HIDDEN_SERVER_OBJECTS) {
	    MessageCreated nextMessageCreated = getMessageCreated(object);
	    if (nextMessageCreated != null) {
		List<MessagePropertable> messageProperties = getMessageProperties(object);
		objectMessaged = new ObjectMessaged(nextMessageCreated, messageProperties);
	    }
	}
	return objectMessaged;
    }

    private List<MessagePropertable> getMessageProperties(AbstractGameObject object) {
	List<MessagePropertable> propertiesList = new LinkedList<>();
	propertiesList.add(new MessagePositionChanged(object.getPosition(), object.getId()));
	switch (object.getType()) {
	    case ITEM:
		StaticItem item = (StaticItem) object;
		HashMap<String, Object> states = item.getVariableProperties().getStates();
		Set<Map.Entry<String, Object>> entrySet = states.entrySet();
		for (Map.Entry<String, Object> entry : entrySet) {
		    switch (entry.getKey()) {
			case "rotate": {
			    //@working wtf
			    Object value = entry.getValue();
			    if (value != null) {
				propertiesList.add(new MessagePropertySet(item.getId(), "rotate", value));
			    }
			}
			break;
		    }
		}
		break;
	    case PLAYER:
		Player player = (Player) object;
		propertiesList.add(new MessageNicknameSet(player.getId(), player.getNickname()));
		propertiesList.add(new MessageInventorySet(player.getId(), player.getInventoryComponent().getSlots()));
		break;
	}
	return propertiesList;
    }

    private MessageCreated getMessageCreated(AbstractGameObject object) {
	MessageCreated nextMessageCreated = null;
	switch (object.getType()) {
	    case ITEM:
		StaticItem item = (StaticItem) object;
		int id = item.getProperties().getId();
		nextMessageCreated = new MessageCreatedItem(id);
		break;
	    case PLAYER:
		Player player = (Player) object;
		nextMessageCreated = new MessageCreated(player.getType());
		break;
	}
	return nextMessageCreated;
    }

    private void waitForAllToRoger() {
	while (!pendingRogers.isEmpty()) {
	    waitSomeTime();
	    for (int i = 0; i < pendingRogers.size(); i++) {
		Client client = pendingRogers.get(i);
		if (client.isRogered()) {
		    pendingRogers.remove(i);
		    client.setRogered(false);
		}
	    }
	}
    }

    private void waitForToRoger(Connection connection) {
	Client clientByConnection = getClientByConnection(connection);
	waitForToRoger(clientByConnection);
    }

    private void waitForToRoger(Client client) {
	while (!client.isRogered()) {
	    waitSomeTime();
	}
	client.setRogered(false);
    }

    private void waitSomeTime() {
	try {
	    Thread.sleep(100L);
	} catch (InterruptedException ex) {
	    Logs.error("server", "While trying to sleep in network thread", ex);
	}
    }
}
