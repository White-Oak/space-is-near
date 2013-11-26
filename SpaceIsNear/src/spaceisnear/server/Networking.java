/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.server;

import com.esotericsoftware.kryonet.*;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import spaceisnear.game.bundles.Bundle;
import spaceisnear.game.bundles.MessageBundle;
import spaceisnear.game.messages.MessageType;
import spaceisnear.game.messages.NetworkableMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import spaceisnear.game.bundles.ObjectBundle;
import spaceisnear.game.messages.MessageClientInformation;
import spaceisnear.game.messages.MessageConnectionBroken;
import spaceisnear.game.messages.MessageCreated;
import spaceisnear.game.messages.MessageMapSent;
import spaceisnear.game.messages.MessageMoved;
import spaceisnear.game.messages.MessageWorldSent;
import spaceisnear.server.objects.GameObject;
import spaceisnear.server.objects.Player;
import static spaceisnear.Utils.GSON;

/**
 * @author white_oak
 */
@RequiredArgsConstructor public class Networking extends Listener implements Runnable {

    public Server server;
    private final ServerCore core;
    private final ArrayList<Connection> connections = new ArrayList<>();
    private MessageClientInformation informationAboutLastConnected;
    private boolean[] rogered;

    @Override
    public void received(Connection connection, Object object) {
	if (object instanceof MessageBundle) {
	    MessageBundle bundle = (MessageBundle) object;
	    MessageType mt = bundle.messageType;
	    byte[] b = bundle.bytes;
	    switch (mt) {
		case CLIENT_INFO:
		    informationAboutLastConnected = MessageClientInformation.getInstance(b);
		    System.out.println("Client information received");
		    break;
		case ROGERED:
		    connections.toArray(new Connection[connections.size()]);
		    for (int i = 0; i < connections.size(); i++) {
			Connection connection1 = connections.get(i);
			if (connection1.equals(connection)) {
			    rogered[i] = true;
			    System.out.println(i + " rogered");
			}
		    }
		    break;
		case MOVED:
		    MessageMoved mm = MessageMoved.getInstance(b);
		    core.getContext().sendToID(mm, mm.getId());
		    sendToAll(mm);
		    break;
	    }
//	    System.out.println("Message received");
	} else if (object instanceof String) {
	    System.out.println(object);
	}
    }

    public void sendToAll(NetworkableMessage message) {
	Bundle b = message.getBundle();
	server.sendToAllTCP(b);
//	System.out.println("Message sent");
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
	player.setNickname(informationAboutLastConnected.getDesiredNickname());
	//2
	ObjectBundle bundle = player.getBundle();
	MessageCreated messageCreated = new MessageCreated(GSON.toJson(bundle));
	return messageCreated;
    }

    public void host() throws IOException {
	server = new Server(10445718, 10445718);
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
	//@working r36, r37, r38, r39
	//1. send the world to the new player
	//2. add a player
	//3. send MessageCreated of Player to all connections
	//1
	MessageWorldSent messageWorldSent = getWorldInOneJSON();
	sendToID(connections.size() - 1, messageWorldSent);
	System.out.println("World has sent " + messageWorldSent.getWorld().length());
	MessageMapSent messageMapSent = getTiledLayerInOneJSON();
	sendToID(connections.size() - 1, messageMapSent);
	System.out.println("Map has sent " + messageMapSent.getMap().length());
	//2
	MessageCreated messageCreated = createPlayerAndPrepare();
	//waiting for client to process world
	waitForToRoger(rogered.length - 1);
	rogered[rogered.length - 1] = false;
	//3
	sendToAll(messageCreated);
	System.out.println("Player has sent");
	//wait for client to receive his player
	waitForAllToRoger();
	resetRogeredStatuses();
	//
	core.unpause();
	System.out.println("Server has continued his work");
    }

    private MessageWorldSent getWorldInOneJSON() {
	GameContext context = core.getContext();
	List<GameObject> objects = context.getObjects();
	List<MessageCreated> messages = new ArrayList<>();
	for (GameObject object : objects) {
	    messages.add(new MessageCreated(GSON.toJson(object.getBundle())));
	}
	return new MessageWorldSent(GSON.toJson(messages.toArray(new MessageCreated[messages.size()])));
    }

    private MessageMapSent getTiledLayerInOneJSON() {
	return new MessageMapSent(GSON.toJson(core.getTiledLayer().getMap()));
    }

    private boolean isRogeredByAll() {
	boolean result = true;
	for (int i = 0; i < rogered.length; i++) {
	    boolean b = rogered[i];
	    result &= b;
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
	    Logger.getLogger(Networking.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
}
