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
import spaceisnear.game.ui.console.LogString;
import spaceisnear.server.objects.Player;
import spaceisnear.server.objects.items.*;

/**
 * @author white_oak
 */
@RequiredArgsConstructor public class ServerNetworking extends Listener implements Runnable {

    public Server server;
    private final ServerCore core;
    private final ArrayList<Connection> connections = new ArrayList<>();
    private final ArrayList<Player> players = new ArrayList<>();
    private MessageClientInformation informationAboutLastConnected;
    private boolean[] rogered;
    private final static MessageBundle ROGER_REQUSTED_BUNDLE = new MessageRogerRequested().getBundle();
    private final Queue<MessageBundle> messages = new LinkedList<>();

    private List<MessagePropertable> propertys;
    private List<MessagePropertable> propertysForNewPlayer;

    @Override
    public void received(Connection connection, Object object) {
	if (object instanceof MessageBundle) {
	    MessageBundle bundle = (MessageBundle) object;
	    MessageType mt = bundle.messageType;
	    switch (mt) {
		case ROGERED:
		    connections.toArray(new Connection[connections.size()]);
		    for (int i = 0; i < connections.size(); i++) {
			Connection connection1 = connections.get(i);
			if (connection1.equals(connection)) {
			    rogered[i] = true;
			}
		    }
		    break;
		default:
		    messages.add(bundle);
		    break;
	    }
	} else if (object instanceof String) {
	    System.out.println(object);
	}
    }

    public void processReceivedQueue() {
	while (!messages.isEmpty()) {
	    processBundle(messages.poll());
	}
    }

    private void processBundle(MessageBundle bundle) {
	MessageType mt = bundle.messageType;
	byte[] b = bundle.bytes;
	switch (mt) {
	    case CLIENT_INFO:
		informationAboutLastConnected = MessageClientInformation.getInstance(b);
		System.out.println("Client information received");
		break;
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
	sendToConnection(connections.get(id), bundle);
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
	if (!core.isAlreadyPaused()) {
	    core.pause();
	    connections.add(connection);
	    rogered = new boolean[connections.size()];
	    new Thread(this).start();
	} else {
	    sendToConnection(connection, new MessageConnectionBroken());
	    connection.close();
	}
    }

    private void createPlayer() {
	//1
	Player player = core.addPlayer(connections.size());
	players.add(player);
	player.setNickname(informationAboutLastConnected.getDesiredNickname());
	final ServerContext context = core.getContext();
	final ServerItemsArchive itemsArchive = ServerItemsArchive.ITEMS_ARCHIVE;
	final int idByName = itemsArchive.getIdByName("ear_radio");
	StaticItem item = ItemAdder.addItem(new Position(-1, -1), idByName, context);
	player.getInventoryComponent().getSlots().getEar().setItemId(item.getId());
    }

    public void host() throws IOException {
	server = new Server(256 * 1024, 1024);
	Registerer.registerEverything(server);
	server.start();
	server.addListener(this);
	server.bind(54555);
    }

    @Override
    public void run() {
	processNewPlayer();
    }

    private void processNewPlayer() {
	waitForServerToStop();
	//create list of properties
	propertys = new ArrayList<>();
	int lastConnected = connections.size() - 1;
	//
	createPlayer();
	sendWorld(lastConnected);
	sendPlayer(lastConnected);
	for (int i = 0; i < connections.size(); i++) {
	    sendProperties(i);
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
	Player get = players.get(players.size() - 1);
	String message = get.getNickname() + " has connected to SIN!";
//	log(new LogString(message, LogLevel.BROADCASTING, "145.9"));
    }

    private void waitForServerToStop() {
	//wait for client to send information about clien
	//and for server to finally pause
	//@done use this info
	while (informationAboutLastConnected == null || !core.isAlreadyPaused()) {
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

    private void sendWorld(int lastConnected) {
	MessageCreated[] world = getWorld();
	MessageWorldInformation mwi = new MessageWorldInformation(world.length, propertys.size());
	sendToID(lastConnected, mwi);
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
			sendToID(lastConnected, cloned);
			cloned = null;
		    }
		}
	    } else {
		if (cloned != null) {
		    sendToID(lastConnected, cloned);
		    cloned = null;
		}
	    }
	    sendToID(lastConnected, messageCreated);
	}
	if (cloned != null) {
	    sendToID(lastConnected, cloned);
	}
    }

    private void sendPlayer(int lastConnected) {
	MessageYourPlayerDiscovered mypd = new MessageYourPlayerDiscovered(players.get(players.size() - 1).getId());
	sendToID(lastConnected, mypd);
	System.out.println("Player has sent");
    }

    private void sendProperties(int lastConnected) {
	for (MessagePropertable messagePropertable : propertys) {
	    sendToID(lastConnected, messagePropertable);
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
