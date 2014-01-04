// Generated by delombok at Sun Dec 01 13:28:22 MSK 2013
package spaceisnear.game;

import com.esotericsoftware.kryonet.*;
import com.google.gson.JsonSyntaxException;
import spaceisnear.game.bundles.*;
import spaceisnear.game.messages.*;
import java.io.IOException;
import spaceisnear.LoadingScreen;
import spaceisnear.game.objects.Player;
import spaceisnear.game.objects.ClientGameObject;
import spaceisnear.server.Registerer;
import static spaceisnear.Utils.GSON;
import spaceisnear.game.objects.items.StaticItem;

/**
 * @author LPzhelud
 */
public class Networking extends Listener {

    private final GameContext gameContext;
    private Client client;
    private final static MessageRogered ROGERED = new MessageRogered();

    public void connect(String host, int tcpPort) throws IOException {
	client = new Client(256 * 1024, 2 * 1024);
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
		    processMessageMoved(mm);
		    break;
		case PAUSED:
		    processMessagePaused();
		    break;
		case UNPAUSED:
		    processMessageUnpaused();
		    break;
		case CREATED:
		    MessageCreated mc = MessageCreated.getInstance(b);
		    processMessageCreated(mc);
		    break;
		case DIED:
		    processMessageDied();
		    break;
		case MAP_SENT:
		    MessageMapSent mms = MessageMapSent.getInstance(b);
		    processMessageMapSent(mms);
		    break;
		case DISCOVERED_PLAYER:
		    MessageYourPlayerDiscovered dypm = MessageYourPlayerDiscovered.getInstance(b);
		    processDiscoveredYourPlayerMessage(dypm);
		    break;
		case ROGER_REQUESTED:
		    send(ROGERED);
		    break;
		case CREATED_SIMPLIFIED:
		    MessageCreatedItem mci = MessageCreatedItem.getInstance(b);
		    processMessageCreatedItem(mci);
		    break;
		case WORLD_INFO:
		    MessageWorldInformation mwi = MessageWorldInformation.getInstance(b);
		    processMessageWorldInformation(mwi);
		    break;
	    }
//	    System.out.println("Message received");
	}
    }

    private void processMessageWorldInformation(MessageWorldInformation mwi) {
	LoadingScreen.LOADING_AMOUNT = mwi.amountOfItems;
	LoadingScreen.CURRENT_AMOUNT = 0;
    }

    private void processMessageCreatedItem(MessageCreatedItem mci) {
	StaticItem item = StaticItem.getInstance(mci.getId(), mci.getP(), gameContext);
	if (item != null) {
	    gameContext.addObject(item);
	    LoadingScreen.CURRENT_AMOUNT++;
	}
    }

    private void processDiscoveredYourPlayerMessage(MessageYourPlayerDiscovered dypm) {
	gameContext.setNewGamerPlayer(dypm.getPlayerID());
    }

    private void processMessageMapSent(MessageMapSent mms) throws JsonSyntaxException {
	int[][] map = GSON.fromJson(mms.getMap(), int[][].class);
	gameContext.getCameraMan().getTiledLayer().setMap(map);
	gameContext.getCameraMan().delegateWidth();
	System.out.println("got tiled layer");
    }

    private void processMessageMoved(MessageMoved mm) {
	gameContext.sendToID(mm, mm.getId());
    }

    private void processMessagePaused() {
	gameContext.getCore().pause();
    }

    private void processMessageCreated(MessageCreated mc) throws JsonSyntaxException {
	ObjectBundle ob = (ObjectBundle) (GSON.fromJson(mc.getJson(), ObjectBundle.class));
	ClientGameObject gameObject;
	gameObject = getObjectFromBundle(ob);
	if (gameObject != null) {
	    gameContext.addObject(gameObject);
	    LoadingScreen.CURRENT_AMOUNT++;
	}
    }

    private void processMessageUnpaused() {
	gameContext.getCore().unpause();
    }

    private void processMessageDied() {
    }

    private ClientGameObject getObjectFromBundle(ObjectBundle ob) {
	ClientGameObject gameObject = null;
	switch (ob.getObjectType()) {
	    case PLAYER:
		gameObject = Player.getInstance(ob, gameContext);
		break;
	    case ITEM:
		gameObject = StaticItem.getInstance(ob, gameContext);
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

}
