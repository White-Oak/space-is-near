package spaceisnear.game.ui.console;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import lombok.Getter;
import spaceisnear.abstracts.Context;
import spaceisnear.game.GameContext;
import spaceisnear.game.messages.MessageLog;
import spaceisnear.game.messages.MessageToSend;
import spaceisnear.game.messages.properties.MessagePropertySet;
import spaceisnear.game.objects.GamerPlayer;
import spaceisnear.game.ui.TextField;

/**
 *
 * @author White Oak
 */
public class GameConsole extends Actor {

    private final int x, y, width, height;
    private final TextField ip;
    private final InGameLog log;
//    Font font = new TrueTypeFont(awtFont, false);
    private final Context context;
    private int scrollBarSize;
    private int scrollBarY;
    private boolean scrollBarClicked;
    @Getter private final BitmapFont font;
    //

    public GameConsole() {
	this.x = 0;
	this.y = 0;
	this.width = 0;
	this.height = 0;
	this.ip = null;
	this.log = null;
	this.context = null;
	this.font = null;
    }

    public GameConsole(int x, int y, int width, int height, Context context, TextField tf) {
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
	font = new BitmapFont(Gdx.files.classpath("default.fnt"), false);
	ip = tf;
	log = new InGameLog(830, 2, width - 30, (int) (height - 2 - ip.getHeight()));
	this.context = context;
	scrollBarSize = sizeOfScrollBar();
	camera.setToOrtho(true);
	camera.update();
	renderer.setProjectionMatrix(camera.combined);
    }
    private final ShapeRenderer renderer = new ShapeRenderer();
    private final OrthographicCamera camera = new OrthographicCamera(1200, 600);

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
	renderer.translate(x, 0, 0);
	batch.end();
	renderer.begin(ShapeRenderer.ShapeType.FilledRectangle);
	renderer.setColor(Color.WHITE);
	renderer.filledRect(0, 0, width, height);
	//left scrollbar 
	renderer.setColor(Color.GRAY);
	renderer.filledRect(0, 2 + scrollBarY, 20, scrollBarSize);
	renderer.setColor(Color.WHITE);
	renderer.filledRect(0, 0, 2, height);
	renderer.translate(-x, 0, 0);
	renderer.end();
	batch.begin();
	//
	log.paint(batch, getLineByScrollBarY());
	//
    }

    private void sendMessageFromPlayer(String message) {
	GamerPlayer player = ((GameContext) context).getPlayer();
	String nickname = player.getNickname();
	LogString logString = new LogString(nickname + ": " + message, LogLevel.TALKING, player.getPosition());
	MessageToSend messageToSend = new MessageToSend(new MessageLog(logString));
	context.sendDirectedMessage(messageToSend);
    }

    private void processInputedMessage() {
	String text = ip.getText();
	if (text.startsWith("-")) {
	    String substring = text.substring(1);
	    String[] split = substring.split(" ");
	    switch (split[0]) {
		case "debug":
		    processDebugRequestMessage(split);
		    break;
		case "stoppull":
		    MessagePropertySet messagePropertySet = new MessagePropertySet(((GameContext) context).getPlayerID(), "pull", -1);
		    MessageToSend messageToSend = new MessageToSend(messagePropertySet);
		    context.sendDirectedMessage(messageToSend);
		    break;
		case "h":
		    if (split.length > 2) {
			processBroadcastingMessageFromPlayer(split[1], split);
		    }
		    break;
	    }
	} else {
	    sendMessageFromPlayer(text);
	}
	ip.setText("");
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
		StringBuilder sb = new StringBuilder();
		for (int i = 2; i < message.length; i++) {
		    String string = message[i];
		    sb.append(string);
		    sb.append(' ');
		}
		standAloneMessage = sb.toString();
	    }
	    LogString logString = new LogString(nickname + ": " + standAloneMessage, LogLevel.BROADCASTING, frequency);
	    MessageToSend messageToSend = new MessageToSend(new MessageLog(logString));
	    context.sendDirectedMessage(messageToSend);
	}
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
	log.pushMessage(str);
	scrollBarSize = sizeOfScrollBar();
    }

    public void mouseClicked(int button, int x, int y, int clickCount) {

    }

    public void mousePressed(int button, int x, int y) {
	if (x > this.x && x < this.x + 20) {
	    if (y > scrollBarY && y < scrollBarY + scrollBarSize) {
		scrollBarClicked = true;
	    }
	}
    }

    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
	if (scrollBarClicked) {
	    int move = newy - oldy;
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

    public void mouseReleased(int button, int x, int y) {
	scrollBarClicked = false;
    }

    public boolean intersects(int x, int y) {
	boolean xB = x > this.x && x < x + width;
	boolean yB = y > this.y && y < y + height;
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
	int unseenLines = log.getLinesNumber() - linesPerHeight(font, height);
	return (int) (unseenLines * multiplier);
    }

    private int sizeOfGameLog() {
	return (int) (height - ip.getHeight() - 4);
    }

    public void mouseWheelMoved(int newValue) {
	processDrag(-newValue >> 2);
    }
}
