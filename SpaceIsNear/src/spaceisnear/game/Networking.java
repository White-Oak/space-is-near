package spaceisnear.game;

import com.esotericsoftware.kryonet.*;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import lombok.*;
import spaceisnear.abstracts.AbstractGameObject;
import spaceisnear.game.bundles.*;
import spaceisnear.game.messages.*;
import spaceisnear.game.messages.properties.MessageNicknameSet;
import spaceisnear.game.messages.properties.MessageYourPlayerDiscovered;
import spaceisnear.game.messages.service.MessageNetworkState;
import spaceisnear.game.messages.service.MessageRogered;
import spaceisnear.game.messages.service.onceused.*;
import spaceisnear.game.objects.*;
import spaceisnear.game.objects.items.ItemsArchive;
import spaceisnear.game.objects.items.StaticItem;
import spaceisnear.game.ui.console.LogLevel;
import spaceisnear.game.ui.console.LogString;
import spaceisnear.server.Registerer;
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
    @Getter private boolean logined, joined;

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
	Bundle bundle = message.getBundle();
	if (client != null && client.isConnected()) {
	    client.sendTCP(bundle);
	}
//	System.out.println("Message sent");
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
	core.getContext().sendThemAll(new MessageNetworkState(3));
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
		case DIED:
		    processMessageDied();
		    break;
		case DISCOVERED_PLAYER:
		    MessageYourPlayerDiscovered dypm = MessageYourPlayerDiscovered.getInstance(b);
		    processDiscoveredYourPlayerMessage(dypm);
		    joined = true;
		    break;
		case ROGER_REQUESTED:
		    send(ROGERED);
		    break;
		case CREATED_SIMPLIFIED: {
		    MessageCreated mc = MessageCreated.getInstance(b);
		    processMessageCreated(mc);
		}
		break;
		case CREATED_SIMPLIFIED_ITEM:
		    MessageCreatedItem mci = MessageCreatedItem.getInstance(b);
		    processMessageCreatedItem(mci);
		    break;
		case WORLD_INFO:
		    MessageWorldInformation mwi = MessageWorldInformation.getInstance(b);
		    processMessageWorldInformation(mwi);
		    break;
		case LOG:
		    MessageLog ml = MessageLog.getInstance(b);
		    core.log(ml.getLog());
		    break;
		case TELEPORTED:
		    MessageTeleported mte = MessageTeleported.getInstance(b);
		    core.getContext().sendToID(mte, mte.getId());
		    break;
		case NICKNAME_SET:
		    MessageNicknameSet mns = MessageNicknameSet.getInstance(b);
		    processMessageNicknameSet(mns);
		    break;
		case INVENTORY_SET:
		    MessageInventorySet mis = MessageInventorySet.getInstance(b);
		    GamerPlayer player = core.getContext().getPlayer();
		    player.getInventoryComponent().getSlots().setSlots(mis.getSlots());
		    break;
		case CLONED:
		    MessageCloned mc = Message.createInstance(b, MessageCloned.class);
		    processMessageCloned(mc);
		    break;
		case ACCESS:
		    MessageAccess ma = Message.createInstance(b, MessageAccess.class);
		    logined = ma.isAccess();
		    if (!logined) {
			core.log(new LogString("Incorrect pair of login/password", LogLevel.WARNING));
		    }
		    break;
	    }
//	    System.out.println("Message received");
//	    gameContext.getCore().log(new LogString("Message received", LogLevel.DEBUG));
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
	GamerPlayer player = core.getContext().getPlayer();
	player.setNickname(mns.getNickname());
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
