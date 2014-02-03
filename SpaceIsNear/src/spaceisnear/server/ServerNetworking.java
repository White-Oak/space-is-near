package spaceisnear.server;

import com.esotericsoftware.kryonet.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import lombok.*;
import spaceisnear.*;
import spaceisnear.game.bundles.*;
import spaceisnear.game.components.*;
import spaceisnear.game.messages.*;
import spaceisnear.game.messages.properties.*;
import spaceisnear.game.messages.service.*;
import spaceisnear.game.messages.service.onceused.*;
import spaceisnear.game.objects.*;
import spaceisnear.game.ui.console.*;
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
    private final static MessageRogerRequested ROGER_REQUSTED = new MessageRogerRequested();
    private final Queue<MessageBundle> messages = new LinkedList<>();

    private List<MessagePropertable> propertys;

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
//		    System.out.println("Somebody controlled his player");
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
	core.getContext().log(log);
	switch (log.getLevel()) {
	    case TALKING:
		processIncomingTalkingLogMessage(log);
		break;
	    case BROADCASTING:
		int[][] bufferMap = core.getContext().getAvailabilityMatrixOfHearingFrequency(log.getFrequency());
		for (int i = 0; i < players.size(); i++) {
		    Player player = players.get(i);
		    if (bufferMap[player.getPosition().getX()][player.getPosition().getY()] > 0) {
			sendToConnection(connections.get(i), new MessageLog(log));
		    }
		}
		break;
	}
    }

    private void processIncomingTalkingLogMessage(final LogString log) {
	for (int i = 0; i < players.size(); i++) {
	    Player player = players.get(i);
	    Position positionToHear = player.getPosition();
	    Position positionToSay = log.getPosition();
	    if (core.getContext().isHearingLogMessage(positionToSay, positionToHear)) {
		sendToConnection(connections.get(i), new MessageLog(log));
	    }
	}
    }

    public void sendToAll(NetworkableMessage message) {
	Bundle b = message.getBundle();
	server.sendToAllTCP(b);
//	System.out.println("Message sent");
    }

    public void sendToID(int id, NetworkableMessage message) {
	Bundle b = message.getBundle();
	if (b.getBytes() != null && b.getBytes().length > 2048) {
	    System.out.println("l");
	}
	sendToID(id, b);
    }

    private void sendToID(int id, Bundle bundle) {
	sendToConnection(connections.get(id), bundle);
    }

    private void sendToConnection(Connection c, NetworkableMessage message) {
	Bundle b = message.getBundle();
	sendToConnection(c, b);
    }

    private void sendToConnection(Connection c, Bundle bundle) {
	server.sendToTCP(c.getID(), bundle);
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

    private MessageCreated createPlayerAndPrepare() {
	//1
	Player player = core.addPlayer(connections.size());
	players.add(player);
	player.setNickname(informationAboutLastConnected.getDesiredNickname());
	//2
	MessageCreated messageCreated = new MessageCreated(GameObjectType.PLAYER);
	//properties
	propertys.add(new MessageNicknameSet(player.getId(), player.getNickname()));
	MessageTeleported messageTeleported = new MessageTeleported(player.getPosition(), player.getId());
	propertys.add(messageTeleported);
	return messageCreated;
    }

    public void host() throws IOException {
	server = new Server(256 * 1024, 1 * 1024);
	Registerer.registerEverything(server);
	server.start();
	server.addListener(this);
	server.bind(54555);
    }

    @Override
    public void run() {
	waitForServerToStop();
	//create list of properties
	propertys = new ArrayList<>();
	int lastConnected = connections.size() - 1;
	//1. send the world to the new player
	//2. add a player
	//3. send MessageCreated of Player to all connections
	//1
	sendWorld(lastConnected);
	//2,3
	sendPlayer(lastConnected);
	//
	core.unpause();
	System.out.println("Server has continued his work");
	//wait for client to unpause
	orderEveryoneToRogerAndWait();
	//@working fix that
	for (int i = 0; i < 10; i++) {
	    waitSomeTime();
	}
	Player get = players.get(players.size() - 1);
	String message = get.getNickname() + " has connected to SIN!";
	log(new LogString(message, LogLevel.BROADCASTING, "145.9"));
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
	sendToAll(ROGER_REQUSTED);
	waitForAllToRoger();
	resetRogeredStatuses();
    }

    private void sendWorld(int lastConnected) {
	MessageCreated[] world = getWorld();
	MessageWorldInformation mwi = new MessageWorldInformation(world.length + 1, propertys.size());
	sendToID(lastConnected, mwi);
	for (int i = 0; i < world.length; i++) {
	    MessageCreated messageCreated = world[i];
	    sendToID(lastConnected, messageCreated);
	    if (i % 125 == 0) {
//		System.out.println("I've just sent a chunk of items");
		sendToID(lastConnected, ROGER_REQUSTED);
		waitForToRoger(lastConnected);
		rogered[lastConnected] = false;
	    }
	}
	sendProperties(lastConnected);
    }

    private void sendPlayer(int lastConnected) {
	//2
	MessageCreated messageCreated = createPlayerAndPrepare();
	MessageYourPlayerDiscovered mypd = new MessageYourPlayerDiscovered(core.getContext().getObjects().size() - 1);
	//3
	sendToAll(messageCreated);
	sendToID(lastConnected, mypd);
	for (int i = 0; i < propertys.size(); i++) {
	    MessagePropertable messagePropertable = propertys.get(i);
	    sendToAll(messagePropertable);
	    if (i % 125 == 0) {
//		System.out.println("I've just sent a chunk of items");
		sendToAll(ROGER_REQUSTED);
		waitForToRoger(lastConnected);
		rogered[lastConnected] = false;
	    }
	}
	//
	System.out.println("Player has sent");
	//wait for client to receive his player
	orderEveryoneToRogerAndWait();
    }

    private void sendProperties(int lastConnected) {
	//sending properties
	for (int i = 0; i < propertys.size(); i++) {
	    MessagePropertable messagePropertable = propertys.get(i);
	    sendToID(lastConnected, messagePropertable);
	    if (i % 125 == 0) {
//		System.out.println("I've just sent a chunk of items");
		sendToID(lastConnected, ROGER_REQUSTED);
		waitForToRoger(lastConnected);
		rogered[lastConnected] = false;
	    }
	}
	propertys = new ArrayList<>();
    }

    private MessageCreated[] getWorld() {
	ServerContext context = core.getContext();
	List<AbstractGameObject> objects = context.getObjects();
	List<MessageCreated> messagesToReturn = new ArrayList<>();
	for (AbstractGameObject object : objects) {
	    if (object != null && object.getId() >= ServerContext.HIDDEN_SERVER_OBJECTS) {
		if (object.getType() == GameObjectType.ITEM) {
		    StaticItem item = (StaticItem) object;
		    int id = item.getProperties().getId();
		    messagesToReturn.add(new MessageCreatedItem(id));
		    //properties
		    HashMap<String, ComponentState> states = item.getVariableProperties().getStates();
		    Set<Map.Entry<String, ComponentState>> entrySet = states.entrySet();
		    for (Map.Entry<String, ComponentState> entry : entrySet) {
			switch (entry.getKey()) {
			    case "rotate": {
				int value = (int) entry.getValue().getValue();
				if (value != 0) {
				    propertys.add(new MessagePropertySet(item.getId(), "rotate", value));
				}
			    }
			    break;
			    case "stucked": {
				boolean stucked = (boolean) entry.getValue().getValue();
				if (stucked != item.getProperties().getBundle().stuckedByAddingFromScript) {
				    propertys.add(new MessagePropertySet(item.getId(), "stucked", stucked));
				}
			    }
			    break;
			}
		    }
		} else {
		    messagesToReturn.add(new MessageCreated(object.getType()));
		    //properties
		}
		//properties
		MessageTeleported messageTeleported = new MessageTeleported(object.getPosition(), object.getId());
		propertys.add(messageTeleported);
	    }
	}
	return messagesToReturn.toArray(new MessageCreated[messagesToReturn.size()]);
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
