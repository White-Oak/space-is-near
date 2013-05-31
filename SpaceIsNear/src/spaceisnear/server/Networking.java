/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import java.io.IOException;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import spaceisnear.game.bundles.Bundle;
import spaceisnear.game.bundles.MessageBundle;
import spaceisnear.game.bundles.ObjectBundle;
import spaceisnear.game.messages.MessageType;
import spaceisnear.game.messages.NetworkableMessage;

/**
 *
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
	}else if(object instanceof ObjectBundle){
	    
	}
    }

    public void send(NetworkableMessage message) {
	MessageType mt = message.getMessageType();
	Bundle b = message.getBundle();
	server.sendToAllTCP(b);
	System.out.println("Message sent");
    }

    @Override
    public void connected(Connection connection) {
	connections.add(connection);
	core.pause();
    }

    public void host() throws IOException {
	server = new Server();
	registerEverything();
	server.start();
	server.addListener(this);
	server.bind(54555);
    }
}
