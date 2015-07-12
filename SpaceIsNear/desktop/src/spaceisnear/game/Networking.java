package spaceisnear.game;

import com.esotericsoftware.kryonet.*;
import com.esotericsoftware.kryonet.Client;
import java.io.*;
import java.util.ArrayDeque;
import java.util.Queue;
import lombok.*;
import spaceisnear.game.messages.*;
import spaceisnear.game.messages.properties.MessagePropertable;
import spaceisnear.game.messages.service.*;
import spaceisnear.game.messages.service.onceused.*;
import spaceisnear.server.*;

/**
 * @author LPzhelud
 */
@RequiredArgsConstructor public class Networking extends Listener {

    private final Engine engine;
    private Client client;
    private final static MessageRogered ROGERED = new MessageRogered();
    @Getter @Setter private MessagePlayerInformation mpi;
    @Getter @Setter private MessageLogin mci;
    @Setter @Getter private boolean logined, joined, playable;
    private final Queue<Message> messages = new ArrayDeque<>(10);

    public void connect(String host, int tcpPort) throws IOException {
	client = new Client(ServerNetworking.BUFFER_SIZE, ServerNetworking.O_BUFFER_SIZE);
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
	    client.sendTCP(message);
	}
    }

    @Override
    public void connected(Connection connection) {
//	context.sendThemAll(new MessageNetworkState(1));
    }

    @Override
    public void disconnected(Connection connection) {
//	context.sendThemAll(new MessageNetworkState(2));
    }

    @Override
    public void idle(Connection connection) {
//	core.getContext().sendThemAll(new MessageNetworkState(3));
    }

    @Override
    public void received(Connection connection, Object object) {
	if (object instanceof Message) {
	    messages.add((Message) object);
	}
    }

    public void processQueue() {
	while (!messages.isEmpty()) {
	    processMessage(messages.poll());
	}
    }

    private void processMessage(Message message) {
	if (message != null) {
	    MessageType mt = message.getMessageType();
	    switch (mt) {
		case ROGER_REQUESTED:
		    send(ROGERED);
		    break;
		case JOINED:
		    joined = true;
		    break;
		default:
		    final GameContext context = engine.getContext();
		    if (message instanceof MessagePropertable) {
			MessagePropertable mp = (MessagePropertable) message;
			if (mp.canBeApplied(context)) {
			    mp.processForClient(context);
			} else {
			    context.sendDirectedMessage(new MessageNotReadyYet(mp));
			}
		    } else {
			message.processForClient(context);
		    }
		    break;
	    }
	}
    }

    public void close() {
	if (client != null) {
	    client.close();
	}
    }

}
