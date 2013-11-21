/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.*;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import spaceisnear.game.bundles.Bundle;
import spaceisnear.game.bundles.MessageBundle;
import spaceisnear.game.messages.MessageType;
import spaceisnear.game.messages.NetworkableMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import spaceisnear.game.bundles.JSONBundle;
import spaceisnear.game.bundles.ObjectBundle;
import spaceisnear.game.messages.MessageClientInformation;
import spaceisnear.game.messages.MessageConnectionBroken;
import spaceisnear.game.messages.MessageCreated;
import spaceisnear.server.objects.GameObject;
import spaceisnear.server.objects.Player;

/**
 * @author white_oak
 */
@RequiredArgsConstructor public class Networking extends Listener implements Runnable {

    public Server server;
    private final ServerCore core;
    private ArrayList<Connection> connections = new ArrayList<>();
    private MessageClientInformation informationAboutLastConnected;
    private boolean[] rogered;

    @Override
    public void received(Connection connection, Object object) {
	if (object instanceof MessageBundle) {
	    MessageBundle bundle = (MessageBundle) object;
	    MessageType mt = MessageType.values()[bundle.messageType];
	    byte[] b = bundle.bytes;
	    switch (mt) {
		case CLIENT_INFO:
		    MessageClientInformation mci = MessageClientInformation.getInstance(b);
		    break;
	    }

	    System.out.println("Message received");
	}
    }

    public void sendToAll(NetworkableMessage message) {
	Bundle b = message.getBundle();
	server.sendToAllTCP(b);
	System.out.println("Message sent");
    }

    public void sendToID(int id, NetworkableMessage message) {
	Bundle b = message.getBundle();
	sendToID(id, b);
    }

    private void sendToID(int id, Bundle bundle) {
	sendToConnection(connections.get(id), bundle);
    }

    private void sendToConnection(Connection c, Bundle bundle) {
	server.sendToTCP(c.getID(), bundle);
	System.out.println("Message sent");
    }

    private void sendToConnection(Connection c, NetworkableMessage message) {
	Bundle b = message.getBundle();
	sendToConnection(c, b);
    }

    @Override
    public synchronized void connected(Connection connection) {
	if (!core.isAlreadyPaused()) {
	    core.pause();
	    connections.add(connection);
	    rogered = new boolean[connections.size()];
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
	Gson gson = new Gson();
	MessageCreated messageCreated = new MessageCreated(gson.toJson(bundle));
	return messageCreated;
    }

    public void host() throws IOException {
	server = new Server();
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
	while (informationAboutLastConnected != null && !core.isAlreadyPaused()) {
	    waitSomeTime();
	}
	//@working r36, r37, r38, r39
	//1. send the world to the new player
	//2. add a player
	//3. send MessageCreated of Player to all connections
	//1
	JSONBundle worldInOneJSON = getWorldInOneJSON();
	sendToID(connections.size() - 1, worldInOneJSON);
	//2
	MessageCreated messageCreated = createPlayerAndPrepare();
	//waiting for client to process world
	waitForToRoger(rogered.length - 1);
	rogered[rogered.length - 1] = false;
	//3
	sendToAll(messageCreated);
	//wait for client to receive his player
	waitForAllToRoger();
	resetRogeredStatuses();
	//
	core.unpause();
    }

    private JSONBundle getWorldInOneJSON() {
	GameContext context = core.getContext();
	List<GameObject> objects = context.getObjects();
	List<MessageCreated> messages = new ArrayList<>();
	for (GameObject object : objects) {
	    messages.add(new MessageCreated(new Gson().toJson(object.getBundle())));
	}
	Object[] objectss = new Object[]{messages.toArray(new MessageCreated[messages.size()]), core.getTiledLayer()};
	return new JSONBundle(new Gson().toJson(objectss));
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
	for (int i = 0; i < rogered.length; i++) {
	    rogered[i] = false;
	}
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
