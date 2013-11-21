package spaceisnear.game;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.*;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import spaceisnear.game.bundles.*;
import spaceisnear.game.messages.*;

import java.io.IOException;
import spaceisnear.game.objects.GamerPlayer;
import spaceisnear.game.objects.Player;
import spaceisnear.game.objects.GameObject;
import static spaceisnear.game.objects.GameObjectType.PLAYER;

/**
 * @author LPzhelud
 */
@RequiredArgsConstructor public class Networking extends Listener {

    private final GameContext gameContext;
    private Client client;
    private boolean justConnected = true;

    public void connect(String host, int tcpPort) throws IOException {
	client = new Client();
	registerEverything();
	client.start();
	client.addListener(this);
	client.connect(5000, host, tcpPort);
	send(new MessageClientInformation("fuck you"));
    }

    public void send(NetworkableMessage message) {
	Bundle bundle = message.getBundle();
	if (client != null && client.isConnected()) {
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
		case PAUSED:
		    gameContext.getCore().pause();
		    break;
		case CREATED:
		    MessageCreated mc = MessageCreated.getInstance(b);
		    ObjectBundle ob = (ObjectBundle) (new Gson().fromJson(mc.getJson(), ObjectBundle.class));
		    GameObject gameObject = null;
		    if (justConnected) {
			justConnected = false;
			Player player = GamerPlayer.getInstance(ob, gameContext);
			gameObject = player;
			send(new MessageRogered());
		    } else {
			gameObject = getObjectFromBundle(ob);
		    }
		    if (gameObject != null) {
			gameContext.addObject(gameObject);
		    }
		    break;
		case DIED:
		    break;
	    }
	    System.out.println("Message received");
	} else if (object instanceof JSONBundle) {
	    //World received
	    MessageCreated[] messages = new Gson().fromJson(((JSONBundle) object).getBody(), MessageCreated[].class);
	    for (int i = 0; i < messages.length; i++) {
		MessageCreated mc = messages[i];
		ObjectBundle ob = (ObjectBundle) (new Gson().fromJson(mc.getJson(), ObjectBundle.class));
		GameObject gameObject = getObjectFromBundle(ob);
		if (gameObject != null) {
		    gameContext.addObject(gameObject);
		}
	    }
	    send(new MessageRogered());
	}
    }

    private GameObject getObjectFromBundle(ObjectBundle ob) {
	GameObject gameObject = null;
	switch (ob.getObjectType()) {
	    case PLAYER:
		gameObject = Player.getInstance(ob, gameContext);
		break;
	}
	return gameObject;
    }

    public void close() {
	if (client != null) {
	    client.close();
	}
    }

    public void registerEverything() {
	register(Bundle.class);
	register(MessageBundle.class);
	register(byte[].class);
    }

    private void register(Class classs) {
	Kryo kryo = null;
	if (client != null) {
	    kryo = client.getKryo();
	}
	kryo.register(classs);
    }
}
