// Generated by delombok at Sun Dec 01 13:28:22 MSK 2013
package spaceisnear.game;

import com.esotericsoftware.kryonet.*;
import spaceisnear.game.bundles.*;
import spaceisnear.game.messages.*;
import java.io.IOException;
import spaceisnear.game.objects.GamerPlayer;
import spaceisnear.game.objects.Player;
import spaceisnear.game.objects.ClientGameObject;
import spaceisnear.server.Registerer;
import static spaceisnear.Utils.GSON;

/**
 * @author LPzhelud
 */
public class Networking extends Listener {

    private final GameContext gameContext;
    private Client client;
    private boolean justConnected = true;

    public void connect(String host, int tcpPort) throws IOException {
	client = new Client(104457, 104457);
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
		    ClientGameObject gameObject;
		    if (justConnected) {
			System.out.println("got some first object");
			GamerPlayer player = GamerPlayer.getInstance(ob, gameContext);
			gameContext.setPlayerID(player.getId());
			gameObject = player;
			justConnected = false;
		    } else {
			gameObject = getObjectFromBundle(ob);
		    }
		    if (gameObject != null) {
			gameContext.addObject(gameObject);
			System.out.println("added player");
			send(new MessageRogered());
		    }
		    break;

		case DIED:
		    break;

		case WORLD_SENT:
		    MessageWorldSent mws = MessageWorldSent.getInstance(b);
		    MessageCreated[] messages = GSON.fromJson(mws.getWorld(), MessageCreated[].class);
		    for (MessageCreated messageCreated : messages) {
			ObjectBundle ob1 = (ObjectBundle) (GSON.fromJson(messageCreated.getJson(), ObjectBundle.class));
			ClientGameObject gameObject1 = getObjectFromBundle(ob1);
			if (gameObject1 != null) {
			    gameContext.addObject(gameObject1);
			}
		    }
		    System.out.println("got all objects");
		    break;

		case MAP_SENT:
		    MessageMapSent mms = MessageMapSent.getInstance(b);
		    int[][] map = GSON.fromJson(mms.getMap(), int[][].class);
		    gameContext.getCameraMan().getTiledLayer().setMap(map);
		    gameContext.getCameraMan().delegateWidth();
		    send(new MessageRogered());
		    System.out.println("got tiled layer");
		    break;

	    }
//	    System.out.println("Message received");
	}
    }

    private ClientGameObject getObjectFromBundle(ObjectBundle ob) {
	ClientGameObject gameObject = null;
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

    @java.beans.ConstructorProperties({"gameContext"})
    public Networking(final GameContext gameContext) {

	this.gameContext = gameContext;
    }

    public boolean isJustConnected() {
	return this.justConnected;
    }
}
