package spaceisnear.game.ui.console;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.*;
import com.esotericsoftware.minlog.Logs;
import lombok.Getter;
import spaceisnear.game.GameContext;
import spaceisnear.game.messages.MessageLog;
import spaceisnear.game.messages.MessageToSend;
import spaceisnear.game.messages.properties.MessagePropertySet;
import spaceisnear.game.messages.service.onceused.MessageClientInformation;
import spaceisnear.game.objects.GamerPlayer;
import spaceisnear.game.ui.TextField;

/**
 *
 * @author White Oak
 */
public class GameConsole extends Actor {

    private final int x, y;
    @Getter private final TextField textField;
    private final InGameLog log;
//    Font font = new TrueTypeFont(awtFont, false);
    private final GameContext context;
    private int scrollBarSize;
    private int scrollBarY;
    private boolean scrollBarClicked;
    @Getter private final BitmapFont font;
    //

    public GameConsole(int x, int y, int width, int height, GameContext context, TextField tf) {
	this.x = x;
	this.y = y;
	setWidth(width);
	setHeight(height - tf.getHeight());
	font = new BitmapFont(Gdx.files.classpath("segoe_ui.fnt"), false);
	textField = tf;
	log = new InGameLog(830, 2, width - 30, (int) (height - 2 - textField.getHeight()));
	this.context = context;
	scrollBarSize = sizeOfScrollBar();
	camera.setToOrtho(true);
	camera.update();
	renderer.setProjectionMatrix(camera.combined);
	addListener(new InputListener() {

	    @Override
	    public boolean touchDown(InputEvent event, float x, float y, int p, int pt) {
		mouseClicked((int) x, (int) y);
		return true;
	    }

	    @Override
	    public void touchDragged(InputEvent event, float x, float y, int pointer) {
		mouseDragged((int) x, (int) y);
	    }

	    @Override
	    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
		mouseReleased((int) x, (int) y);
	    }

	});
    }
    private final ShapeRenderer renderer = new ShapeRenderer();
    private final OrthographicCamera camera = new OrthographicCamera(1200, 600);

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {

	renderer.translate(x, 0, 0);
	batch.end();
	renderer.begin(ShapeRenderer.ShapeType.Filled);
	renderer.setColor(new Color(0xdce0e1ff));
	renderer.rect(0, 0, getWidth(), getHeight());
	//left scrollbar 
	//underneath
	renderer.setColor(new Color(0xfcf7f7ff));
	renderer.rect(getWidth() - 18, 0, 18, getHeight());
	//actual scrollbar
	renderer.setColor(new Color(0x7f8c8dff));
	renderer.rect(getWidth() - 12, 5 + scrollBarY, 6, scrollBarSize - 10);
	renderer.end();
	//SHADOWS
	renderer.begin(ShapeRenderer.ShapeType.Line);
	renderer.setColor(new Color(0xbcc0c1ff));
	renderer.line(0, 0, 0, getHeight() + 100);
//	renderer.line(0, getHeight() - 2, getWidth(), getHeight() - 2);
	renderer.setColor(new Color(0xd0d7d8ff));
	renderer.line(-1, 0, -1, getHeight() + 100);
	renderer.end();
	//
//	renderer.begin(ShapeRenderer.ShapeType.Line);
//	renderer.setColor(Color.BLACK);
//	renderer.line(0, 0, 0, getHeight());
//	renderer.end();
	renderer.translate(-x, 0, 0);
	batch.begin();
	//
	log.paint(batch, getLineByScrollBarY());
	//
    }

    private void sendMessageFromPlayer(String message) {
	GamerPlayer player = ((GameContext) context).getPlayer();
	String nickname = player.getNickname();
	message = nickname + " says: " + message;
	LogString logString = new LogString(message, LogLevel.TALKING, player.getPosition());
	sendLogString(logString);
    }

    public void processInputedMessage() {
	String text = textField.getText();
	if (context.isLogined()) {
	    if (context.isPlayable()) {
		if (text.startsWith("-")) {
		    processControlSequence(text);
		} else {
		    sendMessageFromPlayer(text);
		}
	    } else {
		sendOOC(text);
	    }
	} else {
	    pushMessage(new LogString("You cannot write messages while not connected!", LogLevel.WARNING));
	}
	textField.setText("");
    }

    private void sendOOC(String text) {
	MessageClientInformation mci = context.getCore().getNetworking().getMci();
	text = mci.getLogin() + ": " + text;
	LogString logString = new LogString(text, LogLevel.OOC);
	sendLogString(logString);
    }

    private void sendWhisper(String message) {
	GamerPlayer player = context.getPlayer();
	String nickname = player.getNickname();
	message = nickname + " whispers: " + message;
	LogString logString = new LogString(message, LogLevel.WHISPERING, player.getPosition());
	sendLogString(logString);
    }

    private void processControlSequence(String text) {
	String substring = text.substring(1);
	String[] split = substring.split(" ");
	String cs = split[0];
	String message = substring.substring(split[0].length() + 1);
	switch (split[0]) {
	    case "d":
	    case "debug":
		processDebugRequestMessage(split);
		break;
	    case "stoppull":
		MessagePropertySet messagePropertySet = new MessagePropertySet(context.getPlayerID(), "pull", -1);
		MessageToSend messageToSend = new MessageToSend(messagePropertySet);
		context.sendDirectedMessage(messageToSend);
		break;
	    case "broadcast":
	    case "h":
		if (split.length > 2) {
		    processBroadcastingMessageFromPlayer(split[1], split);
		}
		break;
	    case "ooc":
		sendOOC(message);
		break;
	    case "w":
	    case "whisper":
		sendWhisper(message);
		break;
	    case "pm":
		if (split.length > 1) {
		    sendPM(split[1], toStandAloneString(split, 2));
		}
		break;
	}
    }

    private void sendPM(String receiver, String message) {
	try {
	    int receiverID = receiver.equals("me") ? context.getPlayerID() : Integer.parseInt(receiver);
	    Logs.debug("client", "Receiver is " + receiverID);
	    StringBuilder stringBuilder = new StringBuilder(20);
	    stringBuilder.append('[').append(context.getPlayerID()).append("] -> [").append(receiverID).append("] ")
		    .append(context.getPlayer().getNickname()).append(" messages: ").append(message);
	    LogString logString = new LogString(stringBuilder.toString(), LogLevel.PRIVATE, receiverID);
	    sendLogString(logString);
	} catch (NumberFormatException numberFormatException) {
	    pushMessage(new LogString(receiver + " is not a correct ID", LogLevel.WARNING));
	}
    }

    private void sendLogString(LogString logString) {
	MessageToSend messageToSend = new MessageToSend(new MessageLog(logString));
	context.sendDirectedMessage(messageToSend);
    }

    private void processBroadcastingMessageFromPlayer(String frequency, String[] message) {
	if (frequency.equals("all")) {
	    frequency = "145.9";
	}
	if (isGoodFrequency(frequency)) {
	    GamerPlayer player = ((GameContext) context).getPlayer();
	    String nickname = player.getNickname();
	    String standAloneMessage;
	    if (message.length == 3) {
		standAloneMessage = message[2];
	    } else {
		standAloneMessage = toStandAloneString(message, 2);
	    }
	    LogString logString = new LogString(nickname + " broadcasts: " + standAloneMessage, LogLevel.BROADCASTING, frequency);
	    sendLogString(logString);
	}
    }

    private String toStandAloneString(String[] message, int start) {
	String standAloneMessage;
	StringBuilder sb = new StringBuilder();
	for (int i = start; i < message.length; i++) {
	    String string = message[i];
	    sb.append(string);
	    sb.append(' ');
	}
	standAloneMessage = sb.toString().trim();
	return standAloneMessage;
    }

    private boolean isGoodFrequency(String frequency) {
	String[] split1 = frequency.split("\\.");
	if (split1.length == 2) {
	    if (split1[0].length() <= 3 && split1[1].length() == 1) {
		String regex = "[0-9]+";
		if (split1[0].matches(regex)) {
		    if (split1[1].length() == 1) {
			return split1[1].matches(regex);
		    }
		}
	    }
	}
	return false;
    }

    private void processDebugRequestMessage(String[] split) {
	if (split.length > 1) {
	    switch (split[1]) {
		case "on":
		    log.setAcceptDebugMessages(true);
		    break;
		case "off":
		    log.setAcceptDebugMessages(false);
		    break;
	    }
	}
    }

    public boolean hasFocus() {
//	return ip.hasFocus();
	return false;
    }

    public void pushMessage(LogString str) {
	log.pushMessage(str, context);
	scrollBarSize = sizeOfScrollBar();
    }
    private int oldy;

    public void mouseClicked(int x, int y) {
	if (x > 0 && x > getWidth() - 18) {
	    if (y > scrollBarY && y < scrollBarY + scrollBarSize) {
		scrollBarClicked = true;
		oldy = y;
	    }
	}
    }

    public void mouseDragged(int newx, int newy) {
	if (scrollBarClicked) {
	    int move = newy - oldy;
	    oldy = newy;
	    processDrag(move);
	}
    }

    private void processDrag(int move) {
	scrollBarY += move;
	if (scrollBarY + scrollBarSize > sizeOfGameLog()) {
	    scrollBarY = sizeOfGameLog() - scrollBarSize;
	} else if (scrollBarY < 0) {
	    scrollBarY = 0;
	}
    }

    public void mouseReleased(int x, int y) {
	scrollBarClicked = false;
    }

    public boolean intersects(int x, int y) {
	boolean xB = x > this.x && x < x + getWidth();
	boolean yB = y > this.y && y < y + getHeight();
	return xB && yB;
    }

    private int linesPerHeight(BitmapFont f, int height) {
	return height / (int) f.getLineHeight();
    }

    private int sizeOfScrollBar() {
	float multiplier = ((float) linesPerHeight(font, sizeOfGameLog())) / log.size();
	if (multiplier > 1) {
	    multiplier = 1;
	}
	return (int) (multiplier * sizeOfGameLog());
    }

    private int getLineByScrollBarY() {
	float multiplier = scrollBarY / (float) (sizeOfGameLog() - sizeOfScrollBar());
	int unseenLines = log.getLinesNumber() - linesPerHeight(font, (int) getHeight());
	return (int) (unseenLines * multiplier);
    }

    private int sizeOfGameLog() {
	return (int) (getHeight());
    }

    public void mouseWheelMoved(int newValue) {
	processDrag(-newValue >> 2);
    }

    public void setAcceptDebugMessages(boolean acceptDebugMessages) {
	log.setAcceptDebugMessages(acceptDebugMessages);
    }

}
