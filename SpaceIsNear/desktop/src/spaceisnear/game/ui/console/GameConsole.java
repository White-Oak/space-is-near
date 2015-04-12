package spaceisnear.game.ui.console;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.*;
import lombok.Getter;
import lombok.Setter;
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
    private int scrollBarSize;
    private int scrollBarY;
    private boolean scrollBarClicked;
    @Getter private final BitmapFont font;
    //
    @Getter @Setter private ConsoleListener consoleListener;

    public GameConsole(int x, int y, int width, int height, TextField tf) {
	this.x = x;
	this.y = y;
	setWidth(width);
	setHeight(height - tf.getHeight());
	font = new BitmapFont(Gdx.files.internal("segoe_ui.fnt"), false);
	textField = tf;
	log = new InGameLog(830, 2, width - 60, (int) (height - 2 - textField.getHeight()));
	scrollBarSize = sizeOfScrollBar();
	camera.setToOrtho(true);
	camera.update();
	renderer.setProjectionMatrix(camera.combined);
	addListener(new InputListener() {

	    @Override
	    public boolean touchDown(InputEvent event, float x, float y, int p, int pt) {
		System.out.println("WHATS UP");
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
    public void draw(Batch batch, float parentAlpha) {

	renderer.translate(x, 0, 0);
	batch.end();
	renderer.begin(ShapeRenderer.ShapeType.Filled);
	renderer.setColor(podlozhkaColor);
	renderer.rect(0, 0, getWidth(), getHeight());
	//left scrollbar 
	//underneath
	renderer.setColor(scrollbarPodlozhkaColor);
	renderer.rect(getWidth() - 18, 0, 18, getHeight());
	//actual scrollbar
	renderer.setColor(scrollbarColor);
	renderer.rect(getWidth() - 12, 5 + scrollBarY, 6, scrollBarSize - 10);
	renderer.end();
	//SHADOWS
	renderer.begin(ShapeRenderer.ShapeType.Line);
	renderer.setColor(shadowColor);
	renderer.line(0, 0, 0, getHeight() + 100);
//	renderer.line(0, getHeight() - 2, getWidth(), getHeight() - 2);
//	renderer.setColor(new Color(0xd0d7d8ff));
//	renderer.line(-1, 0, -1, getHeight() + 100);
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
    private final Color shadowColor = new Color(0xbcc0c1ff);
    private final Color scrollbarColor = new Color(0x7f8c8dff);
    private final Color scrollbarPodlozhkaColor = new Color(0xfcf7f7ff);
    private final Color podlozhkaColor = new Color(0xdce0e1ff);

    public void processInputMessage() {
	String text = textField.getText();
	if (consoleListener != null) {
	    consoleListener.processInputMessage(text, this);
	}
	textField.setText("");
    }

    public boolean hasFocus() {
//	return ip.hasFocus();
	return false;
    }

    public void pushMessage(ChatString str) {
	log.pushMessage(str);
	scrollBarSize = sizeOfScrollBar();
	if (!scrollBarClicked) {
	    scrollBarY = sizeOfGameLog() - scrollBarSize;
	}
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
