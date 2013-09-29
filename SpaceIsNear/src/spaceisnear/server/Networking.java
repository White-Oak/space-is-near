/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.*;
import lombok.RequiredArgsConstructor;
import spaceisnear.game.bundles.Bundle;
import spaceisnear.game.bundles.MessageBundle;
import spaceisnear.game.messages.MessageType;
import spaceisnear.game.messages.NetworkableMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import spaceisnear.server.objects.Player;

/**
 * @author white_oak
 */
@RequiredArgsConstructor public class Networking extends Listener {

    public Server server;
    private final ServerCore core;
    private ArrayList<Connection> connections = new ArrayList<>();

    public void registerEverything() {
	register(Bundle.class);
	register(MessageBundle.class);
	register(byte[].class);
    }

    private void register(Class classs) {
	Kryo kryo = server.getKryo();
	kryo.register(classs);
    }

    @Override
    public void received(Connection connection, Object object) {
	if (object instanceof MessageBundle) {
	    MessageBundle bundle = (MessageBundle) object;
	    MessageType mt = MessageType.values()[bundle.messageType];
	    byte[] b = bundle.bytes;
	    switch (mt) {
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
	server.sendToTCP(connections.get(id).getID(), b);
	System.out.println("Message sent");
    }

    @Override
    public synchronized void connected(Connection connection) {
	//@working r36
	//1. add a player
	//2. send MessageCreated of GamerPlayer to connection
	//3. send MessageCreated of xxPlayer to other connections

	//1.
	connections.add(connection);
	core.pause();
	while (!core.isAlreadyPaused()) {
	    try {
		Thread.sleep(50L);
	    } catch (InterruptedException ex) {
		Logger.getLogger(Networking.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
	connection.getRemoteAddressTCP().toString();
	Player player = core.addPlayer(connections.size());
	//2.
	
    }

    public void host() throws IOException {
	server = new Server();
	registerEverything();
	server.start();
	server.addListener(this);
	server.bind(54555);
    }
}
