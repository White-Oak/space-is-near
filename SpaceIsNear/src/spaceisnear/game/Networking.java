package spaceisnear.game;

import com.esotericsoftware.kryonet.*;
import lombok.RequiredArgsConstructor;
import spaceisnear.game.bundles.*;
import spaceisnear.game.messages.*;

import java.io.IOException;
import lombok.Getter;
import spaceisnear.game.objects.GamerPlayer;
import spaceisnear.game.objects.Player;
import spaceisnear.game.objects.GameObject;
import spaceisnear.server.Registerer;
import static spaceisnear.Utils.GSON;

/**
 * @author LPzhelud
 */
@RequiredArgsConstructor public class Networking extends Listener {

    private final GameContext gameContext;
    private Client client;
    @Getter private boolean justConnected = true;

    public void connect(String host, int tcpPort) throws IOException {
	client = new Client(10445718, 10445718);
	Registerer.registerEverything(client);
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
//	System.out.println("Message sent");
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
	    MessageType mt = bundle.messageType;
	    byte[] b = bundle.bytes;
	    switch (mt) {
		case MOVED:
		    MessageMoved mm = MessageMoved.getInstance(b);
		    gameContext.sendToID(mm, mm.getId());
		    break;
		case PAUSED:
		    gameContext.getCore().pause();
		    break;
		case UNPAUSED:
		    gameContext.getCore().unpause();
		    break;
		case CREATED:
		    MessageCreated mc = MessageCreated.getInstance(b);
		    ObjectBundle ob = (ObjectBundle) (GSON.fromJson(mc.getJson(), ObjectBundle.class));
		    GameObject gameObject;
		    if (justConnected) {
			System.out.println("got some first object");
			GamerPlayer player = GamerPlayer.getInstance(ob, gameContext);
			player.setId(ob.getObjectID());
			gameContext.setPlayerID(player.getId());
			gameObject = player;
			send(new MessageRogered());
			justConnected = false;
		    } else {
			gameObject = getObjectFromBundle(ob);
			if (!gameContext.getCore().isNotpaused() && gameObject instanceof Player) {
			    send(new MessageRogered());
			}
		    }
		    if (gameObject != null) {
			gameContext.addObject(gameObject);
		    }
		    break;
		case DIED:
		    break;
		case WORLD_SENT:
		    MessageWorldSent mws = MessageWorldSent.getInstance(b);
		    MessageCreated[] messages = GSON.fromJson(mws.getWorld(), MessageCreated[].class);
		    for (MessageCreated messageCreated : messages) {
			ObjectBundle ob1 = (ObjectBundle) (GSON.fromJson(messageCreated.getJson(),
				ObjectBundle.class));
			GameObject gameObject1 = getObjectFromBundle(ob1);
			if (gameObject1 != null) {
			    gameContext.addObject(gameObject1);
			}
		    }
		    System.out.println("got all objects");
		    break;
		case MAP_SENT:
		    MessageMapSent mms = MessageMapSent.getInstance(b);
		    int[][] map = GSON.fromJson(mms.getMap(), int[][].class);
		    gameContext.getCamera().getTiledLayer().setMap(map);
		    gameContext.getCamera().delegateWidth();
		    send(new MessageRogered());
		    System.out.println("got tiled layer");
		    break;
	    }
//	    System.out.println("Message received");
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
}
