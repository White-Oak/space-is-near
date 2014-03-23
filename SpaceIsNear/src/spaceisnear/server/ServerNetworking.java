package spaceisnear.server;

import com.esotericsoftware.kryonet.*;
import de.ruedigermoeller.serialization.FSTObjectInput;
import de.ruedigermoeller.serialization.FSTObjectOutput;
import java.io.*;
import java.util.*;
import lombok.*;
import spaceisnear.abstracts.AbstractGameObject;
import spaceisnear.abstracts.Context;
import spaceisnear.game.messages.*;
import spaceisnear.game.messages.properties.*;
import spaceisnear.game.messages.service.*;
import spaceisnear.game.messages.service.onceused.*;
import spaceisnear.game.objects.*;
import spaceisnear.game.ui.console.LogLevel;
import spaceisnear.game.ui.console.LogString;
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

    private static byte[] ROGER_REQUSTED_BYTES;

    private final Queue<Message> messages = new LinkedList<>();
    private final Queue<Connection> connectionsForMessages = new LinkedList<>();

    private final AccountManager accountManager = new AccountManager();

    //
    private final List<Client> pendingRogers = new ArrayList<>();

    static {
	final MessageRogerRequested messageRogerRequested = new MessageRogerRequested();
	try (FSTObjectOutput fstObjectOutput = new FSTObjectOutput()) {
	    fstObjectOutput.writeObject(messageRogerRequested);
	    ROGER_REQUSTED_BYTES = fstObjectOutput.getCopyOfWrittenBuffer();
	} catch (Exception ex) {
	    Context.LOG.log(ex);
	}
    }

    @Override
    public void received(Connection connection, Object object) {
	if (object instanceof byte[]) {
	    byte[] b = (byte[]) object;
	    try (FSTObjectInput objectInput = new FSTObjectInput(new ByteArrayInputStream(b))) {
		Message message = (Message) objectInput.readObject();
		switch (message.getMessageType()) {
		    case ROGERED:
			getClientByConnection(connection).setRogered(true);
			break;
		    default:
			if (!core.isPaused()) {
			    connectionsForMessages.add(connection);
			    messages.add(message);
			}
		}
	    } catch (IOException | ClassNotFoundException ex) {
		Context.LOG.log(ex);
	    }
	} else if (object instanceof String) {
	    Context.LOG.log(object);
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
	switch (mt) {
	    case PLAYER_INFO: {
		MessagePlayerInformation mpi = (MessagePlayerInformation) message;
		getClientByConnection(connection).setPlayerInformation(mpi);
		Context.LOG.log("Player information received");
		sendToConnection(connection, new MessageJoined());
		connectedWantsPlayer(getClientByConnection(connection));
	    }
	    break;
	    case CLIENT_INFO: {
		MessageClientInformation mci = (MessageClientInformation) message;
		final Client clientByConnection = getClientByConnection(connection);
		clientByConnection.setClientInformation(mci);
		String messageS = mci.getLogin() + " requested access with password " + mci.getPassword();
		boolean accessible = accountManager.isAccessible(mci.getLogin(), mci.getPassword());
		messageS += accessible ? " and successfully got it." : " but does not seem to provide legal data.";
		core.getContext().logToServerLog(new LogString(messageS, LogLevel.DEBUG));
		sendToConnection(connection, new MessageAccess(accessible));
		for (int i = 0; i < clients.size(); i++) {
		    final Client client = clients.get(i);
		    if (clientByConnection != client) {
			MessageClientInformation clientInformation = client.getClientInformation();
			if (clientInformation.getLogin().equals(mci.getLogin())) {
			    client.setConnection(connection);
			    clients.remove(clientByConnection);
			    sendToConnection(connection, new MessageJoined());
			    Runnable runnable = () -> processOldPlayer(client);
			    new Thread(runnable, "sending world to old player").start();
			    break;
			}
		    }
		}
		Context.LOG.log("Client information received");
	    }
	    break;
	    default:
		message.processForServer(core.getContext());
		break;

	}
//	    Context.LOG.log("Message received");
    }

    public void sendToAll(NetworkableMessage message) {
	try (FSTObjectOutput fstObjectOutput = new FSTObjectOutput()) {
	    fstObjectOutput.writeObject(message);
	    server.sendToAllTCP(fstObjectOutput.getBuffer());
	} catch (IOException ex) {
	    Context.LOG.log(ex);
	}
    }

    public void sendToConnection(Connection connection, NetworkableMessage message) {
	if (messagesSent == MESSAGES_TO_SEND_BEFORE_REQUESTING_ROGERING) {
	    server.sendToTCP(connection.getID(), ROGER_REQUSTED_BYTES);
	    waitForToRoger(connection);
	    messagesSent = 0;
	}
	try (FSTObjectOutput fstObjectOutput = new FSTObjectOutput()) {
	    fstObjectOutput.writeObject(message);
	    server.sendToTCP(connection.getID(), fstObjectOutput.getBuffer());
	    messagesSent++;
	} catch (Exception ex) {
	    Context.LOG.log("Message caused trouble --" + message.getMessageType());
	    Context.LOG.log(ex);
	}

    }

    public void sendToConnectionID(int id, NetworkableMessage message) {
	if (messagesSent == MESSAGES_TO_SEND_BEFORE_REQUESTING_ROGERING) {
	    Connection connection = null;
	    for (Client client : clients) {
		if (client.getConnection().getID() == id) {
		    connection = client.getConnection();
		}
	    }
	    if (connection == null) {
		return;
	    }
	    server.sendToTCP(id, ROGER_REQUSTED_BYTES);
	    waitForToRoger(connection);
	    messagesSent = 0;
	}
	try (FSTObjectOutput fstObjectOutput = new FSTObjectOutput()) {
	    fstObjectOutput.writeObject(message);
	    server.sendToTCP(id, fstObjectOutput.getBuffer());
	    messagesSent++;
	} catch (Exception ex) {
	    Context.LOG.log("Message caused trouble --" + message.getMessageType());
	    Context.LOG.log(ex);
	}

    }

    public void sendToClientID(int id, NetworkableMessage message) {
//	baos.reset();
	id = clients.get(id).getConnection().getID();
	sendToConnectionID(id, message);
    }
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    private int messagesSent;
    private final int MESSAGES_TO_SEND_BEFORE_REQUESTING_ROGERING = 255;

    @Override
    public void disconnected(Connection connection) {//Possibly should rewrite this shit
	synchronized (clients) {
	    Client clientByConnection = getClientByConnection(connection);
	    MessageClientInformation clientInformation = clientByConnection.getClientInformation();
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
	//1
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
	StaticItem item = ItemAdder.addItem(new Position(-1, -1), idByName, context);
	item.setPlayerId(player.getId());
	player.getInventoryComponent().getSlots().get(nameOfInventorySlot).setItemId(item.getId());
	return item;
    }

    public void host() throws IOException {
	server = new Server(256 * 1024, 1024);
	Registerer.registerEverything(server);
	server.start();
	server.addListener(this);
	server.bind(54555);
    }

    private void processOldPlayer(Client client) {
	sendToAll(new MessagePaused());
	Context.LOG.log("Server's been paused");
	sendWorld(client);//	
	orderEveryoneToRogerAndWait();
	sendPlayer(client);
	sendToAll(new MessageUnpaused());
	Context.LOG.log("Server has continued his work");
    }

    private void processNewPlayer(final Client client) {
//	waitForServerToStop();
	//
	sendToAll(new MessagePaused());
	Context.LOG.log("Server\'s been paused");
	List<ObjectMessaged> objectPlayer = createPlayer(client);
	sendWorld(client);
	sendPlayer(client);
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
	Context.LOG.log("Server has continued his work");
	//@working fix that
	Runnable runnable = () -> {
	    for (int i = 0; i < 20; i++) {
		waitSomeTime();
	    }
	    Player get = client.getPlayer();
	    String message = get.getNickname() + " has connected to SIN!";
	    core.getContext().log(new LogString(message, LogLevel.BROADCASTING, "145.9"));
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
	server.sendToAllTCP(ROGER_REQUSTED_BYTES);
	waitForAllToRoger();
    }

    private void sendWorld(Client client) {
	ObjectMessaged[] world = getWorld();
	MessageWorldInformation mwi = new MessageWorldInformation(world.length, 0);
	sendToConnection(client.getConnection(), mwi);
	sendCreatedsOfWorld(world, client.getConnection());
	sendPropertiesOfWorld(world, client.getConnection());
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

    private void sendPropertiesOfWorld(ObjectMessaged[] world, Connection connection) {
	//properties
	for (ObjectMessaged objectMessaged : world) {
	    List<MessagePropertable> propertable = objectMessaged.propertables;
	    sendProperties(propertable, connection);
	}
    }

    private void sendProperties(List<MessagePropertable> propertable, Connection connection) {
	propertable.forEach(messagePropertable -> sendToConnection(connection, messagePropertable));
    }

    private void sendPlayer(Client client) {
	MessageYourPlayerDiscovered mypd = new MessageYourPlayerDiscovered(client.getPlayer().getId());
	sendToConnection(client.getConnection(), mypd);
	Context.LOG.log("Player has sent");
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
	propertiesList.add(new MessageTeleported(object.getPosition(), object.getId()));
	switch (object.getType()) {
	    case ITEM:
		StaticItem item = (StaticItem) object;
		HashMap<String, Object> states = item.getVariableProperties().getStates();
		Set<Map.Entry<String, Object>> entrySet = states.entrySet();
		for (Map.Entry<String, Object> entry : entrySet) {
		    switch (entry.getKey()) {
			case "rotate": {
			    int value = (int) entry.getValue();
			    if (value != 0) {
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
	    Context.LOG.log(ex);
	}
    }
}
