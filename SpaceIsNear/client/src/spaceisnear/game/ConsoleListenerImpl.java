package spaceisnear.game;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
		    processControlSequence(text.substring(1), console);
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
	return frequency.matches("[1-9][0-9]{2}(?:\\.[0-9])?");
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

    private CharSequence construct(String[] tokens, int start) {
	return Stream.of(Arrays.copyOfRange(tokens, start, tokens.length)).collect(Collectors.joining(" "));
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
	String[] tokens = text.trim().split(" ");
	String query = tokens[0];
	switch (query) {
	    case "d":
	    case "debug":
		if (tokens.length > 1) {
		    processDebugRequestMessage(tokens[1], console);
		} else {
		    console.pushMessage(ChatString.warning("you need to specify on/off. For more info try -help"));
		}
		break;
	    case "stoppull":
		MessagePropertySet messagePropertySet = new MessagePropertySet(context.getPlayerID(), "pull", -1);
		MessageToSend messageToSend = new MessageToSend(messagePropertySet);
		context.sendDirectedMessage(messageToSend);
		break;
	    case "broadcast":
	    case "h":
		if (tokens.length > 2) {
		    processBroadcastingMessageFromPlayer(tokens[1], construct(tokens, 2));
		} else {
		    console.pushMessage(ChatString.warning("you need to specify frequency and a text. For more info try -help"));
		}
		break;
	    case "ooc":
		sendOOC(construct(tokens, 1));
		break;
	    case "w":
	    case "whisper":
		sendWhisper(construct(tokens, 1));
		break;
	    case "pm":
		if (tokens.length > 2) {
		    sendPM(tokens[1], construct(tokens, 2), console);
		} else {
		    console.pushMessage(ChatString.warning("you need to specify receiver and a text. For more info try -help"));
		}
		break;
	    case "help":
		byte[] contents = Utils.getContents(getClass().getResourceAsStream("/res/chatHelp"));
		console.pushMessage(ChatString.warning(new String(contents)));
		break;
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
