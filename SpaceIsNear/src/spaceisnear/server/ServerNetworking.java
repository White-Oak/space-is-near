package spaceisnear.server;

import com.esotericsoftware.kryonet.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import lombok.*;
import spaceisnear.abstracts.AbstractGameObject;
import spaceisnear.game.bundles.*;
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

    private boolean[] rogered;
    private final static MessageBundle ROGER_REQUSTED_BUNDLE = new MessageRogerRequested().getBundle();

    private final Queue<MessageBundle> messages = new LinkedList<>();
    private final Queue<Connection> connectionsForMessages = new LinkedList<>();

    private List<MessagePropertable> propertys;

    private final AccountManager accountManager = new AccountManager();

    @Override
    public void received(Connection connection, Object object) {
	if (object instanceof MessageBundle) {
	    synchronized (messages) {
		MessageBundle bundle = (MessageBundle) object;
		connectionsForMessages.add(connection);
		messages.add(bundle);
	    }
	} else if (object instanceof String) {
	    System.out.println(object);
	}
    }

    public void processReceivedQueue() {
	synchronized (messages) {
	    while (!messages.isEmpty()) {
		processBundle(messages.poll(), connectionsForMessages.poll());
	    }
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

    private void processBundle(MessageBundle bundle, Connection connection) {
	MessageType mt = bundle.messageType;
	byte[] b = bundle.bytes;
	switch (mt) {
	    case ROGERED:
		for (int i = 0; i < clients.size(); i++) {
		    Connection connection1 = clients.get(i).getConnection();
		    if (connection1.equals(connection)) {
			rogered[i] = true;
		    }
		}
		break;
	    case PLAYER_INFO:
		MessagePlayerInformation mpi = MessagePlayerInformation.getInstance(b);
		getClientByConnection(connection).setPlayerInformation(mpi);
		System.out.println("Player information received");
		connectedWantsPlayer(getClientByConnection(connection));
		break;
	    case CLIENT_INFO:
		MessageClientInformation mci = Message.createInstance(b, MessageClientInformation.class);
		getClientByConnection(connection).setClientInformation(mci);
		String message = mci.getLogin() + " requested access with password " + mci.getPassword();
		boolean accessible = accountManager.isAccessible(mci.getLogin(), mci.getPassword());
		message += accessible ? " and successfully got it." : " but does not seem to provide legal data.";
		core.getContext().logToServerLog(new LogString(message, LogLevel.DEBUG));
		sendToConnection(connection, new MessageAccess(accessible));
		System.out.println("Client information received");
	    case CONTROLLED:
		MessageControlledByInput mc = MessageControlledByInput.getInstance(b);
		core.getContext().sendDirectedMessage(mc);
		break;
	    case MOVED:
		MessageMoved mm = MessageMoved.getInstance(b);
		core.getContext().sendToID(mm, mm.getId());
		sendToAll(mm);
		break;
	    case LOG:
		MessageLog ml = MessageLog.getInstance(b);
		final LogString log = ml.getLog();
		log(log);
		break;
	    case PROPERTY_SET:
		MessagePropertySet mps = MessagePropertySet.getInstance(b);
		System.out.println(mps.getName() + " " + mps.getValue().getClass().getName());
		core.getContext().sendDirectedMessage(mps);
		break;

	}
//	    System.out.println("Message received");
    }

    public void log(final LogString log) {
	System.out.println("Checking out!");
	core.getContext().log(log);
    }

    public void sendToAll(NetworkableMessage message) {
	Bundle b = message.getBundle();
	server.sendToAllTCP(b);
    }

    public void sendToAll(Bundle b) {
	server.sendToAllTCP(b);
    }

    public void sendToID(int id, NetworkableMessage message) {
	Bundle b = message.getBundle();
	sendToID(id, b);
    }

    private void sendToID(int id, Bundle bundle) {
	sendToConnection(clients.get(id).getConnection(), bundle);
    }

    private void sendToConnection(Connection c, NetworkableMessage message) {
	Bundle b = message.getBundle();
	sendToConnection(c, b);
    }
    private int messagesSent;
    private final int MESSAGES_TO_SENT_BEFORE_REQUESTING_ROGERING = 255;

    private void sendToConnection(Connection c, Bundle bundle) {
	if (messagesSent == MESSAGES_TO_SENT_BEFORE_REQUESTING_ROGERING) {
	    server.sendToTCP(c.getID(), ROGER_REQUSTED_BUNDLE);
	    waitForToRoger(c.getID() - 1);
	    rogered[c.getID() - 1] = false;
	    messagesSent = 0;
	}
	server.sendToTCP(c.getID(), bundle);
	messagesSent++;
//	System.out.println("Message sent");
    }

    @Override
    public synchronized void connected(Connection connection) {
	synchronized (clients) {
	    Client client = new Client(connection);
	    clients.add(client);
	    rogered = new boolean[clients.size()];
	    //make some kind of queue or something like that to prevent blocking
	    if (core.isAlreadyPaused()) {
		sendToConnection(connection, new MessageConnectionBroken());
		connection.close();
	    }
	}
    }

    private synchronized void connectedWantsPlayer(final Client client) {
	core.pause();
	new Thread(new Runnable() {

	    @Override
	    public void run() {
		processNewPlayer(client);
	    }
	}).start();
    }

    private void createPlayer(Client client) {
	//1
	Player player = core.addPlayer(client.getConnection().getID());
	client.setPlayer(player);
	player.setNickname(client.getPlayerInformation().getDesiredNickname());
	final ServerContext context = core.getContext();
	final ServerItemsArchive itemsArchive = ServerItemsArchive.ITEMS_ARCHIVE;

	int idByName = itemsArchive.getIdByName("ear_radio");
	StaticItem item = ItemAdder.addItem(new Position(-1, -1), idByName, context);
	item.setPlayerId(player.getId());
	player.getInventoryComponent().getSlots().get("ear").setItemId(item.getId());

	idByName = itemsArchive.getIdByName("pda");
	item = ItemAdder.addItem(new Position(-1, -1), idByName, context);
	item.setPlayerId(player.getId());
	player.getInventoryComponent().getSlots().get("right pocket").setItemId(item.getId());
    }

    public void host() throws IOException {
	server = new Server(256 * 1024, 1024);
	Registerer.registerEverything(server);
	server.start();
	server.addListener(this);
	server.bind(54555);
    }

    private void processNewPlayer(Client client) {
	waitForServerToStop();
	//create list of properties
	propertys = new ArrayList<>();
	//
	createPlayer(client);
	sendWorld(client);
	sendPlayer(client);
	for (Client client1 : clients) {
	    sendProperties(client1);
	}
	//
	core.unpause();
	System.out.println("Server has continued his work");
	//wait for client to unpause
	orderEveryoneToRogerAndWait();
	//@working fix that
	for (int i = 0; i < 20; i++) {
	    waitSomeTime();
	}
	Player get = client.getPlayer();
	String message = get.getNickname() + " has connected to SIN!";
	log(new LogString(message, LogLevel.BROADCASTING, "145.9"));
    }

    private void waitForServerToStop() {
	//wait for client to send information about clien
	//and for server to finally pause
	//@done use this info
	while (!core.isAlreadyPaused()) {
	    while (!messages.isEmpty()) {
		waitSomeTime();
	    }
	}
    }

    private void orderEveryoneToRogerAndWait() {
	sendToAll(ROGER_REQUSTED_BUNDLE);
	waitForAllToRoger();
	resetRogeredStatuses();
    }

    private void sendWorld(Client client) {
	MessageCreated[] world = getWorld();
	MessageWorldInformation mwi = new MessageWorldInformation(world.length, propertys.size());
	sendToConnection(client.getConnection(), mwi);
	MessageCloned cloned = null;
	for (int i = 0; i < world.length; i++) {
	    MessageCreated messageCreated = world[i];
	    if (i > 0 && messageCreated instanceof MessageCreatedItem && world[i - 1] instanceof MessageCreatedItem) {
		MessageCreatedItem mci = (MessageCreatedItem) messageCreated;
		MessageCreatedItem mcilast = (MessageCreatedItem) world[i - 1];
		if (mci.getId() == mcilast.getId()) {
		    int amount = cloned == null ? 1 : cloned.amount + 1;
		    cloned = new MessageCloned();
		    cloned.amount = amount;
		    continue;
		} else {
		    if (cloned != null) {
			sendToConnection(client.getConnection(), cloned);
			cloned = null;
		    }
		}
	    } else {
		if (cloned != null) {
		    sendToConnection(client.getConnection(), cloned);
		    cloned = null;
		}
	    }
	    sendToConnection(client.getConnection(), messageCreated);
	}
	if (cloned != null) {
	    sendToConnection(client.getConnection(), cloned);
	}
    }

    private void sendPlayer(Client client) {
	MessageYourPlayerDiscovered mypd = new MessageYourPlayerDiscovered(client.getPlayer().getId());
	sendToConnection(client.getConnection(), mypd);
	System.out.println("Player has sent");
    }

    private void sendProperties(Client client) {
	for (MessagePropertable messagePropertable : propertys) {
	    sendToConnection(client.getConnection(), messagePropertable);
	}
    }

    private MessageCreated[] getWorld() {
	ServerContext context = core.getContext();
	List<AbstractGameObject> objects = context.getObjects();
	List<MessageCreated> messagesToReturn = new ArrayList<>();
	for (AbstractGameObject object : objects) {
	    if (object != null && object.getId() >= ServerContext.HIDDEN_SERVER_OBJECTS) {
		switch (object.getType()) {
		    case ITEM:
			messagesToReturn.add(getMessageCreatedForItem(object));
			break;
		    case PLAYER:
			Player player = (Player) object;
			messagesToReturn.add(getMessageCreatedAndPropertiesForPlayer(player));
			break;
		}
		//properties
		MessageTeleported messageTeleported = new MessageTeleported(object.getPosition(), object.getId());
		propertys.add(messageTeleported);
	    }
	}
	return messagesToReturn.toArray(new MessageCreated[messagesToReturn.size()]);
    }

    private MessageCreated getMessageCreatedAndPropertiesForPlayer(Player player) {
	final MessageCreated messageCreated = new MessageCreated(player.getType());
	//properties
	propertys.add(new MessageNicknameSet(player.getId(), player.getNickname()));
	MessageTeleported messageTeleported = new MessageTeleported(player.getPosition(), player.getId());
	propertys.add(messageTeleported);
	propertys.add(new MessageInventorySet(player.getId(), player.getInventoryComponent().getSlots()));
	return messageCreated;
    }

    private MessageCreated getMessageCreatedForItem(AbstractGameObject object) {
	StaticItem item = (StaticItem) object;
	int id = item.getProperties().getId();

	MessageCreatedItem mci = new MessageCreatedItem(id);
	//properties
	HashMap<String, Object> states = item.getVariableProperties().getStates();
	Set<Map.Entry<String, Object>> entrySet = states.entrySet();
	for (Map.Entry<String, Object> entry : entrySet) {
	    switch (entry.getKey()) {
		case "rotate": {
		    int value = (int) entry.getValue();
		    if (value != 0) {
			propertys.add(new MessagePropertySet(item.getId(), "rotate", value));
		    }
		}
		break;
		case "stucked": {
		    boolean stucked = (boolean) entry.getValue();
		    if (stucked != item.getProperties().getBundle().stuckedByAddingFromScript) {
			propertys.add(new MessagePropertySet(item.getId(), "stucked", stucked));
		    }
		}
		break;
	    }
	}
	return mci;
    }

    private boolean isRogeredByAll() {
	boolean result = true;
	for (int i = 0; i < rogered.length; i++) {
	    result &= rogered[i];
	}
	return result;
    }

    private void waitForAllToRoger() {
	while (!isRogeredByAll()) {
	    waitSomeTime();
	}
    }

    private void resetRogeredStatuses() {
	Arrays.fill(rogered, false);
    }

    private void waitForToRoger(int connectionId) {
	while (!rogered[connectionId]) {
	    waitSomeTime();
	}
    }

    private void waitSomeTime() {
	try {
	    Thread.sleep(100L);
	} catch (InterruptedException ex) {
	    Logger.getLogger(ServerNetworking.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
}
