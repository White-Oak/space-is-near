package spaceisnear.game;

import com.esotericsoftware.kryonet.*;
import com.esotericsoftware.kryonet.Client;
import java.io.*;
import lombok.*;
import spaceisnear.game.messages.*;
import spaceisnear.game.messages.service.*;
import spaceisnear.game.messages.service.onceused.*;
import spaceisnear.server.*;

/**
 * @author LPzhelud
 */
@RequiredArgsConstructor public class Networking extends Listener {

    private final Corev2 core;
    private Client client;
    private final static MessageRogered ROGERED = new MessageRogered();
    @Getter @Setter private MessagePlayerInformation mpi;
    @Getter @Setter private MessageLogin mci;
    @Setter @Getter private boolean logined, joined, playable;

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
//	if (client != null && client.isConnected()) {
//	    try (FSTObjectOutput fstObjectOutput = new FSTObjectOutput()) {
//		fstObjectOutput.writeObject(message);
//		client.sendTCP(fstObjectOutput.getBuffer());
//	    } catch (Exception ex) {
//		Logger.getLogger(ServerNetworking.class.getName()).log(Level.SEVERE, null, ex);
//	    }
//	}
	if (client != null && client.isConnected()) {
	    client.sendTCP(message);
	}
    }

    @Override
    public void connected(Connection connection) {
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
	processMessage((Message) object);
//	if (object instanceof byte[]) {
//	    Message message = null;
//	    try (FSTObjectInput fstObjectInput = new FSTObjectInput(new ByteArrayInputStream((byte[]) object))) {
//		message = (Message) fstObjectInput.readObject();
//	    } catch (IOException | ClassNotFoundException ex) {
//		Logs.error("client", "While trying to read message from the net", ex);
//	    }
//	    processMessage(message);
//	}
    }

    private void processMessage(Message message) {
	if (message != null) {
	    MessageType mt = message.getMessageType();
	    switch (mt) {
		case ROGER_REQUESTED:
//		    Context.LOG.log("No time to explain — roger that!");
		    send(ROGERED);
		    break;
		case JOINED:
		    joined = true;
		    break;
		default:
		    message.processForClient(core.getContext());
		    break;
	    }
	}
//	    Context.LOG.log("Message received");
//	    gameContext.getCore().log(new LogString("Message received", LogLevel.DEBUG));
    }

    public void close() {
	if (client != null) {
	    client.close();
	}
    }

}
