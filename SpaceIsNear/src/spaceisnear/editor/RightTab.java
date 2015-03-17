package spaceisnear.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.*;
import lombok.Getter;
import spaceisnear.game.GameContext;

/**
 *
 * @author White Oak
 */
public class RightTab extends Actor {

    public final static int TILE_HEIGHT = 40, TILE_WIDTH = 40;
    public final static int TILE_PADDING = 5;
    public final static int SCROLLBAR_PADDING_X = 2, SCROLLBAR_PADDING_Y = 2;
    public final static int SCROLLBAR_WIDTH = 15;
    public final static int UI_PADDING_X = SCROLLBAR_WIDTH + SCROLLBAR_PADDING_X,
	    UI_PADDING_Y = SCROLLBAR_PADDING_Y * 2;
    public final static int TAB_WIDTH = UI_PADDING_X + TILE_WIDTH * 2 + TILE_PADDING * 3;
    private final ShapeRenderer renderer = new ShapeRenderer();
    private int scrollBarY;
    @Getter private int chosenOne = 1;
    private ItemsHandler handler = ItemsHandler.HANDLER;

    public RightTab() {
	addCaptureListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {

	    @Override
	    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		x -= UI_PADDING_X;
		x /= TILE_WIDTH + TILE_PADDING * 1.5f;
		y -= UI_PADDING_Y;
		y /= TILE_HEIGHT + TILE_PADDING;
//		Context.LOG.log("Touched: " + x + " " + y);
		//@working
		if (x > 0 && y > 0) {
		    chosenOne = ((int) y) * 2 + (int) x;
		}
		return true;
	    }

	});
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
	batch.end();
	Gdx.graphics.getGL10().glEnable(GL10.GL_BLEND);
	renderer.setProjectionMatrix(batch.getProjectionMatrix());
	renderer.setTransformMatrix(batch.getTransformMatrix());
	renderer.translate(getX(), getY(), 0);
	renderer.begin(ShapeType.Filled);
	renderer.setColor(getColor());
	renderer.rect(0, 0, getWidth(), getHeight());
	//scrollbar
	renderer.setColor(Color.DARK_GRAY);
	renderer.rect(SCROLLBAR_PADDING_X, SCROLLBAR_PADDING_Y + scrollBarY,
		SCROLLBAR_WIDTH, getHeight() - SCROLLBAR_PADDING_Y * 2);
	renderer.end();
	//border
	renderer.begin(ShapeType.Line);
	renderer.setColor(Color.RED);
	int borderX = UI_PADDING_X + TILE_PADDING;
	if (chosenOne % 2 != 0) {
	    borderX += TILE_WIDTH + TILE_PADDING;
	}
	int borderY = UI_PADDING_Y + (TILE_HEIGHT + TILE_PADDING) * (chosenOne / 2);
	renderer.rect(borderX, borderY, TILE_WIDTH + 1, TILE_HEIGHT);
	renderer.end();

	batch.begin();
	for (int i = 0; i < handler.getBundles().length; i++) {
	    int x = (i % 2 == 0 ? 0 : TILE_WIDTH + TILE_PADDING) + TILE_PADDING + UI_PADDING_X
		    + (-GameContext.TILE_WIDTH + TILE_WIDTH) / 2;
	    int y = i / 2 * (TILE_HEIGHT + TILE_PADDING) + UI_PADDING_Y
		    + (-GameContext.TILE_HEIGHT + TILE_HEIGHT) / 2;
	    batch.draw(handler.getTextureRegion(i), getX() + x, getY() + y);
	}
    }

}
