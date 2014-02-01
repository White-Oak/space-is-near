// Generated by delombok at Sun Dec 01 13:28:22 MSK 2013
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */// Generated by delombok at Sun Dec 01 13:28:22 MSK 2013
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.server;

import com.esotericsoftware.kryonet.*;
import spaceisnear.game.bundles.Bundle;
import spaceisnear.game.bundles.MessageBundle;
import spaceisnear.game.messages.MessageType;
import spaceisnear.game.messages.NetworkableMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import spaceisnear.AbstractGameObject;
import spaceisnear.game.messages.service.onceused.MessageClientInformation;
import spaceisnear.game.messages.service.MessageConnectionBroken;
import spaceisnear.game.messages.MessageCreated;
import spaceisnear.game.messages.MessageMoved;
import spaceisnear.server.objects.Player;
import spaceisnear.game.messages.MessageControlledByInput;
import spaceisnear.game.messages.MessageCreatedItem;
import spaceisnear.game.messages.MessageLog;
import spaceisnear.game.messages.properties.MessagePropertySet;
import spaceisnear.game.messages.service.MessageRogerRequested;
import spaceisnear.game.messages.MessageTeleported;
import spaceisnear.game.messages.service.onceused.MessageWorldInformation;
import spaceisnear.game.messages.properties.MessageNicknameSet;
import spaceisnear.game.messages.properties.MessagePropertable;
import spaceisnear.game.messages.properties.MessageYourPlayerDiscovered;
import spaceisnear.game.objects.GameObjectType;
import spaceisnear.game.objects.Position;
import spaceisnear.game.ui.console.LogString;
import spaceisnear.server.objects.items.StaticItem;

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
	//wait for client to send information about clien
	//and for server to finally pause
	//@done use this info
	while (informationAboutLastConnected == null || !core.isAlreadyPaused()) {
	    waitSomeTime();
	}
	//create list of properties
	propertys = new ArrayList<>();
	//@working r36, r37, r38, r39
	//1. send the world to the new player
	//2. add a player
	//3. send MessageCreated of Player to all connections
	//1
	int lastConnected = connections.size() - 1;
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
//	MessageMapSent messageMapSent = getTiledLayerInOneJSON();
//	sendToID(lastConnected, messageMapSent);
//	System.out.println("Map has sent " + messageMapSent.getMap().length());
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
	System.out.println("Player has sent");
	//wait for client to receive his player
	sendToAll(ROGER_REQUSTED);
	waitForAllToRoger();
	resetRogeredStatuses();
	//
	core.unpause();
	System.out.println("Server has continued his work");
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
	List<MessageCreated> messages = new ArrayList<>();
	for (AbstractGameObject object : objects) {
	    if (object != null && object.getId() >= ServerContext.HIDDEN_SERVER_OBJECTS) {
		if (object.getType() == GameObjectType.ITEM) {
		    StaticItem item = (StaticItem) object;
		    int id = item.getProperties().getId();
		    messages.add(new MessageCreatedItem(id));
		    //properties
		} else {
		    messages.add(new MessageCreated(object.getType()));
		    //properties
		}
		//properties
		MessageTeleported messageTeleported = new MessageTeleported(object.getPosition(), object.getId());
		propertys.add(messageTeleported);
	    }
	}
	return messages.toArray(new MessageCreated[messages.size()]);
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
