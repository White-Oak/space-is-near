package spaceisnear.game.ui.console;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.viewport.Viewport;
import lombok.Getter;
import lombok.Setter;
import spaceisnear.game.ui.TextField;

/**
 *
 * @author White Oak
 */
public class GameConsole extends Actor {

    @Getter private final TextField textField;
    private final InGameLog log;
//    Font font = new TrueTypeFont(awtFont, false);
    private int scrollBarSize;
    private int scrollBarY;
    private boolean scrollBarClicked;
    @Getter private final BitmapFont font;
    //

    @Setter private Color shadowColor = new Color(0xbcc0c1ff);
    @Setter private Color scrollbarColor = new Color(0x7f8c8dff);
    @Setter private Color scrollbarPodlozhkaColor = new Color(0xfcf7f7ff);
    @Setter private Color podlozhkaColor = new Color(0xdce0e1ff);
    //
    @Setter private ConsoleListener consoleListener;
    //
    private final ShapeRenderer renderer = new ShapeRenderer();

    public GameConsole(int x, int y, int width, int height, TextField tf, Viewport v) {
	setBounds(x, y, width, height - tf.getHeight());
	font = new BitmapFont(Gdx.files.internal("segoe_ui.fnt"), false);
	textField = tf;
	log = new InGameLog((int) (getX() + 30), (int) (2 + getY()), width - 60, (int) (height - 2 - textField.getHeight()));
	scrollBarSize = sizeOfScrollBar();
	renderer.setProjectionMatrix(v.getCamera().combined);
	addListener(new InputListener() {

	    @Override
	    public boolean touchDown(InputEvent event, float x, float y, int p, int pt) {
//		System.out.println("WHATS UP");
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

    @Override
    public void draw(Batch batch, float parentAlpha) {

	renderer.translate(getX(), 0, 0);
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
	renderer.end();
	//
	renderer.translate(-getX(), 0, 0);
	batch.begin();
	//
	log.paint(batch, getLineByScrollBarY());
	//
    }

    public void processInputedMessage() {
	if (consoleListener != null) {
	    String text = textField.getText();
	    consoleListener.processInputMessage(text, this);
	}

	textField.setText("");
    }

    public void setAcceptDebugMessages(boolean acceptDebugMessages) {
	log.setAcceptDebugMessages(acceptDebugMessages);
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
	boolean xB = x > getX() && x < x + getWidth();
	boolean yB = y > getY() && y < y + getHeight();
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

}
