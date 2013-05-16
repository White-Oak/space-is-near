/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import spaceisnear.game.GameContext;
import spaceisnear.game.messages.MessageNetworkReceived;
import spaceisnear.game.messages.MessageNetworkState;

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
	server.start();
	server.addListener(this);
	server.bind(54555);
    }

    public void connect(String host, int tcpPort) throws IOException {
	client = new Client();
	client.start();
	client.addListener(this);
	client.connect(5000, host, tcpPort);
    }

    public void send(Object data, Class objectType, int id) {
	Bundle bundle = new Bundle(objectType, id, data);
	if (server != null) {
	    server.sendToAllTCP(bundle);
	} else if (client != null) {
	    client.sendTCP(bundle);
	}
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
	gameContext.sendThemAll(new MessageNetworkReceived(object));
    }

    public void close() {
	if (client != null) {
	    client.close();
	} else if (server != null) {
	    server.close();
	}
    }
public void registerEverything(){
    
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

    @AllArgsConstructor private class Bundle {

	@Getter private Class objectType;
	@Getter private int id;
	@Getter private Object message;
    }
}
