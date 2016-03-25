package spaceisnear.game;

import java.util.StringTokenizer;
import me.whiteoak.minlog.Log;
import spaceisnear.Utils;
import spaceisnear.game.messages.MessageChat;
import spaceisnear.game.messages.MessageToSend;
import spaceisnear.game.messages.properties.MessagePropertySet;
import spaceisnear.game.messages.service.onceused.MessageLogin;
import spaceisnear.game.objects.GamerPlayer;
import spaceisnear.game.ui.console.*;

public class ConsoleListenerImpl implements ConsoleListener {

    private final GameContext context;
    private final Engine engine;

    public ConsoleListenerImpl(Engine engine) {
	this.engine = engine;
	this.context = engine.getContext();
    }

    @Override
    public void processInputMessage(String text, GameConsole console) {
	if (context.isLogined()) {
	    if (context.isPlayable()) {
		if (text.startsWith("-")) {
		    processControlSequence(text, console);
		} else {
		    sendMessageFromPlayer(text);
		}
	    } else {
		sendOOC(text);
	    }
	} else {
	    console.pushMessage(new ChatString("You cannot write messages while not connected!", LogLevel.WARNING));
	}
    }

    private boolean isGoodFrequency(String frequency) {
	return frequency.matches("[0-9]{3}[.][0-9]");
    }

    private void processDebugRequestMessage(String split, GameConsole console) {
	switch (split) {
	    case "on":
		console.setAcceptDebugMessages(true);
		break;
	    case "off":
		console.setAcceptDebugMessages(false);
		break;
	}
    }

    private void sendMessageFromPlayer(CharSequence message) {
	GamerPlayer player = ((GameContext) context).getPlayer();
	String nickname = player.getNickname();
	message = nickname + " says: " + message;
	ChatString logString = new ChatString(message.toString(), LogLevel.TALKING, player.getPosition());
	sendLogString(logString);
    }

    private CharSequence construct(StringTokenizer tokenizer) {
	StringBuilder sb = new StringBuilder();
	while (tokenizer.hasMoreElements()) {
	    Object nextElement = tokenizer.nextElement();
	    sb.append(nextElement);
	}
	return sb;
    }

    private void processBroadcastingMessageFromPlayer(String frequency, CharSequence message) {
	if (frequency.equals("all")) {
	    frequency = "145.9";
	}
	if (isGoodFrequency(frequency)) {
	    GamerPlayer player = context.getPlayer();
	    String nickname = player.getNickname();
	    String standAloneMessage = message.toString();
	    ChatString logString = new ChatString(nickname + " broadcasts: " + standAloneMessage, LogLevel.BROADCASTING, frequency);
	    sendLogString(logString);
	}
    }

    private void sendOOC(CharSequence text) {
	MessageLogin mci = engine.getNetworking().getMci();
	text = mci.getLogin() + ": " + text;
	ChatString logString = new ChatString(text.toString(), LogLevel.OOC);
	sendLogString(logString);
    }

    private void sendWhisper(CharSequence message) {
	GamerPlayer player = context.getPlayer();
	String nickname = player.getNickname();
	message = nickname + " whispers: " + message;
	ChatString logString = new ChatString(message.toString(), LogLevel.WHISPERING, player.getPosition());
	sendLogString(logString);
    }

    private void processControlSequence(String text, GameConsole console) {
	String substring = text.substring(1);
	//TODO rework
	StringTokenizer stringTokenizer = new StringTokenizer(substring, " ");
	if (stringTokenizer.hasMoreTokens()) {
	    String query = stringTokenizer.nextToken();
	    switch (query) {
		case "d":
		case "debug":
		    if (stringTokenizer.hasMoreTokens()) {
			processDebugRequestMessage(stringTokenizer.nextToken(), console);
		    }
		    break;
		case "stoppull":
		    MessagePropertySet messagePropertySet = new MessagePropertySet(context.getPlayerID(), "pull", -1);
		    MessageToSend messageToSend = new MessageToSend(messagePropertySet);
		    context.sendDirectedMessage(messageToSend);
		    break;
		case "broadcast":
		case "h":
		    if (stringTokenizer.hasMoreTokens()) {
			processBroadcastingMessageFromPlayer(stringTokenizer.nextToken(), construct(stringTokenizer));
		    }
		    break;
		case "ooc":
		    sendOOC(construct(stringTokenizer));
		    break;
		case "w":
		case "whisper":
		    sendWhisper(construct(stringTokenizer));
		    break;
		case "pm":
		    if (stringTokenizer.hasMoreTokens()) {
			sendPM(stringTokenizer.nextToken(), construct(stringTokenizer), console);
		    }
		    break;
		case "help":
		    byte[] contents = Utils.getContents(getClass().getResourceAsStream("/res/chatHelp"));
		    System.out.println(contents);
		    console.pushMessage(new ChatString(new String(contents), LogLevel.WARNING));
		    break;
	    }
	}
    }

    private void sendPM(String receiver, CharSequence message, GameConsole console) {
	try {
	    int receiverID = receiver.equals("me") ? context.getPlayerID() : Integer.parseInt(receiver);
	    Log.debug("client", "Receiver is " + receiverID);
	    StringBuilder stringBuilder = new StringBuilder(20);
	    stringBuilder.append('[').append(context.getPlayerID()).append("] -> [").append(receiverID).append("] ")
		    .append(context.getPlayer().getNickname()).append(" messages: ").append(message);
	    ChatString logString = new ChatString(stringBuilder.toString(), LogLevel.PRIVATE, receiverID);
	    sendLogString(logString);
	} catch (NumberFormatException numberFormatException) {
	    console.pushMessage(new ChatString(receiver + " is not a correct ID", LogLevel.WARNING));
	}
    }

    private void sendLogString(ChatString logString) {
	MessageToSend messageToSend = new MessageToSend(new MessageChat(logString));
	context.sendDirectedMessage(messageToSend);
    }
}
