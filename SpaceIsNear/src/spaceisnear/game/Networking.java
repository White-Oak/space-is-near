package spaceisnear.game;

import spaceisnear.game.bundles.MessageBundle;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import spaceisnear.game.bundles.Bundle;
import spaceisnear.game.messages.*;

/**
 *
 * @author LPzhelud
 */
@RequiredArgsConstructor public class Networking extends Listener {

    private final GameContext gameContext;
    private Server server;
    private Client client;

    public void host() throws IOException {
	server = new Server();
	registerEverything();
	server.start();
	server.addListener(this);
	server.bind(54555);
    }

    public void connect(String host, int tcpPort) throws IOException {
	client = new Client();
	registerEverything();
	client.start();
	client.addListener(this);
	client.connect(5000, host, tcpPort);
    }

    public void send(NetworkableMessage message) {
	Bundle bundle = message.getBundle();
	if (server != null) {
	    server.sendToAllTCP(bundle);
	} else if (client != null) {
	    client.sendTCP(bundle);
	}
	System.out.println("Message sent");
    }

    @Override
    public void connected(Connection connection) {
	connection.sendTCP("Got it");
	gameContext.sendThemAll(new MessageNetworkState(1));
    }

    @Override
    public void disconnected(Connection connection) {
	gameContext.sendThemAll(new MessageNetworkState(2));
    }

    @Override
    public void idle(Connection connection) {
	gameContext.sendThemAll(new MessageNetworkState(3));
    }

    @Override
    public void received(Connection connection, Object object) {
	if (object instanceof MessageBundle) {
	    MessageBundle bundle = (MessageBundle) object;
	    MessageType mt = MessageType.values()[bundle.messageType];
	    byte[] b = bundle.bytes;
	    switch (mt) {
		case MOVED:
		    MessageMoved mm = MessageMoved.getInstance(b);
		    gameContext.sendToID(mm, mm.getId());
		    break;
	    }

	    System.out.println("Message received");
	}
    }

    public void close() {
	if (client != null) {
	    client.close();
	} else if (server != null) {
	    server.close();
	}
    }

    public void registerEverything() {
	register(Bundle.class);
	register(MessageBundle.class);
	register(byte[].class);
    }

    private void register(Class classs) {
	Kryo kryo = null;
	if (server != null) {
	    kryo = server.getKryo();
	} else if (client != null) {
	    kryo = client.getKryo();
	}
	kryo.register(classs);
    }
}
