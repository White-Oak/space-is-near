package spaceisnear.game;

import com.esotericsoftware.kryonet.*;
import com.esotericsoftware.kryonet.Client;
import com.google.gson.JsonSyntaxException;
import de.ruedigermoeller.serialization.*;
import java.io.*;
import java.util.logging.*;
import lombok.*;
import spaceisnear.abstracts.*;
import spaceisnear.game.messages.*;
import spaceisnear.game.messages.properties.*;
import spaceisnear.game.messages.service.*;
import spaceisnear.game.messages.service.onceused.*;
import spaceisnear.game.objects.*;
import spaceisnear.game.objects.items.*;
import spaceisnear.game.ui.console.*;
import spaceisnear.server.*;
import spaceisnear.starting.LoadingScreen;

/**
 * @author LPzhelud
 */
@RequiredArgsConstructor public class Networking extends Listener {

    private final Corev2 core;
    private Client client;
    private final static MessageRogered ROGERED = new MessageRogered();
    @Getter @Setter private MessagePlayerInformation mpi;
    @Getter @Setter private MessageClientInformation mci;
    @Getter private boolean logined, joined, playable;

    public void connect(String host, int tcpPort) throws IOException {
	client = new Client(256 * 1024, 1024);
	Registerer.registerEverything(client);
	client.start();
	client.addListener(this);
	client.connect(5000, host, tcpPort);
    }

    public boolean isConnected() {
	return client != null;
    }

    public boolean isAbleToTalkIC() {
	return mpi != null;
    }

    public boolean isAbleToTalkOOC() {
	return mci != null;
    }

    public void send(NetworkableMessage message) {
	if (client != null && client.isConnected()) {
	    try (FSTObjectOutput fstObjectOutput = new FSTObjectOutput()) {
		fstObjectOutput.writeObject(message);
		client.sendTCP(fstObjectOutput.getBuffer());
	    } catch (Exception ex) {
		Logger.getLogger(ServerNetworking.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
    }

    @Override
    public void connected(Connection connection) {
	connection.sendTCP("Got it");
	core.getContext().sendThemAll(new MessageNetworkState(1));
    }

    @Override
    public void disconnected(Connection connection) {
	core.getContext().sendThemAll(new MessageNetworkState(2));
    }

    @Override
    public void idle(Connection connection) {
//	core.getContext().sendThemAll(new MessageNetworkState(3));
    }

    @Override
    public void received(Connection connection, Object object) {
	if (object instanceof byte[]) {
	    Message message = null;
	    try (FSTObjectInput fstObjectInput = new FSTObjectInput(new ByteArrayInputStream((byte[]) object))) {
		message = (Message) fstObjectInput.readObject();
	    } catch (IOException | ClassNotFoundException ex) {
		Logger.getLogger(Networking.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    MessageType mt = message.getMessageType();
	    switch (mt) {
		case MOVED:
		    MessageMoved mm = (MessageMoved) message;
//		    Context.LOG.log(mm);
		    processMessageMoved(mm);
		    break;
		case PAUSED:
		    processMessagePaused();
		    break;
		case UNPAUSED:
		    processMessageUnpaused();
		    break;
		case DIED:
		    processMessageDied();
		    break;
		case DISCOVERED_PLAYER:
		    MessageYourPlayerDiscovered dypm = (MessageYourPlayerDiscovered) message;
		    processDiscoveredYourPlayerMessage(dypm);
		    break;
		case ROGER_REQUESTED:
//		    Context.LOG.log("No time to explain — roger that!");
		    send(ROGERED);
		    break;
		case CREATED_SIMPLIFIED: {
		    MessageCreated mc = (MessageCreated) message;
		    processMessageCreated(mc);
		}
		break;
		case CREATED_SIMPLIFIED_ITEM:
		    MessageCreatedItem createdItem = (MessageCreatedItem) message;
		    processMessageCreatedItem(createdItem);
		    break;
		case WORLD_INFO:
		    MessageWorldInformation mwi = (MessageWorldInformation) message;
		    processMessageWorldInformation(mwi);
		    break;
		case LOG:
		    MessageLog ml = (MessageLog) message;
		    core.log(ml.getLog());
		    break;
		case TELEPORTED:
		    MessageTeleported mte = (MessageTeleported) message;
		    core.getContext().sendDirectedMessage(mte);
		    if (core.getContext().getObjects().get(mte.getId()).getType() == GameObjectType.PLAYER
			    || core.getContext().getObjects().get(mte.getId()).getType() == GameObjectType.GAMER_PLAYER) {
			System.out.println(mte);
		    }
		    break;
		case NICKNAME_SET:
		    MessageNicknameSet mns = (MessageNicknameSet) message;
		    processMessageNicknameSet(mns);
		    break;
		case INVENTORY_SET:
		    MessageInventorySet mis = (MessageInventorySet) message;
		    processMessageInventorySet(mis);
		    break;
		case CLONED:
		    MessageCloned mc = (MessageCloned) message;
		    processMessageCloned(mc);
		    break;
		case ACCESS:
		    MessageAccess ma = (MessageAccess) message;
		    processMessageAccess(ma);
		    break;
		case JOINED:
		    joined = true;
		    break;
	    }
//	    Context.LOG.log("Message received");
//	    gameContext.getCore().log(new LogString("Message received", LogLevel.DEBUG));
	}
    }

    private void processMessageAccess(MessageAccess ma) {
	logined = ma.isAccess();
	if (!logined) {
	    core.log(new LogString("Incorrect pair of login/password", LogLevel.WARNING));
	}
    }

    private void processMessageInventorySet(MessageInventorySet mis) {
	AbstractGameObject get = core.getContext().getObjects().get(mis.getId());
	if (get.getType() == GameObjectType.PLAYER || get.getType() == GameObjectType.GAMER_PLAYER) {
	    Player player = (Player) get;
	    player.getInventoryComponent().getSlots().setSlots(mis.getSlots());
	}
    }

    private void processMessageCloned(MessageCloned mc) {
	AbstractGameObject get = core.getContext().getObjects().get(core.getContext().getObjects().size() - 1);
	for (int i = 0; i < mc.amount; i++) {
	    StaticItem item = ItemsArchive.itemsArchive.clone((StaticItem) get);
	    core.getContext().addObject(item);
	    LoadingScreen.CURRENT_AMOUNT++;
	}
    }

    private void processMessageNicknameSet(MessageNicknameSet mns) {
	AbstractGameObject get = core.getContext().getObjects().get(mns.getId());
	if (get.getType() == GameObjectType.PLAYER || get.getType() == GameObjectType.GAMER_PLAYER) {
	    Player player = (Player) get;
	    player.setNickname(mns.getNickname());
	}
    }

    private void processMessageWorldInformation(MessageWorldInformation mwi) {
	LoadingScreen.LOADING_AMOUNT = mwi.amountOfItems;
	LoadingScreen.CURRENT_AMOUNT = 0;
    }

    private void processMessageCreatedItem(MessageCreatedItem mci) {
	StaticItem item = ItemsArchive.itemsArchive.getNewItem(mci.getId());
	if (item != null) {
	    core.getContext().addObject(item);
	    LoadingScreen.CURRENT_AMOUNT++;
	}
    }

    private void processDiscoveredYourPlayerMessage(MessageYourPlayerDiscovered dypm) {
	core.getContext().setNewGamerPlayer(dypm.getPlayerID());
	Context.LOG.log("Your player discovered at " + dypm.getPlayerID());
	playable = true;
    }

    private void processMessageMoved(MessageMoved mm) {
	core.getContext().sendToID(mm, mm.getId());
    }

    private void processMessagePaused() {
	core.getContext().getCore().pause();
    }

    private void processMessageCreated(MessageCreated mc) throws JsonSyntaxException {
	ClientGameObject gameObject = null;
	switch (mc.getType()) {
	    case PLAYER:
		gameObject = new Player();
		break;
	}
	if (gameObject != null) {
	    core.getContext().addObject(gameObject);
	    LoadingScreen.CURRENT_AMOUNT++;
	}
    }

    private void processMessageUnpaused() {
	core.unpause();
    }

    private void processMessageDied() {
    }

    public void close() {
	if (client != null) {
	    client.close();
	}
    }

}
