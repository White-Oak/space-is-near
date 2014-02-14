/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.ui.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import lombok.*;

/**
 *
 * @author White Oak
 */
@RequiredArgsConstructor public class Inventory extends Actor {

    private final static int TILE_HEIGHT = 40, TILE_WIDTH = 40;
    private final static int TILE_PADDING = 5;
    public final static int INVENTORY_WIDTH = (TILE_WIDTH + TILE_PADDING) * 3;
    public final static int INVENTORY_HEIGHT = TILE_PADDING + (TILE_HEIGHT + TILE_PADDING) * 6;
    private int deltaX;
    private final static int DELTA_DELTA_X = 1;
    private final static int MAX_DELTA_X = (TILE_WIDTH + TILE_PADDING) * 2;
    @Getter @Setter private boolean minimized;

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
	batch.end();
	if (minimized) {
	    deltaX += DELTA_DELTA_X;
	    if (deltaX > MAX_DELTA_X) {
		deltaX = MAX_DELTA_X;
	    }
	} else {
	    deltaX -= DELTA_DELTA_X;
	    if (deltaX < 0) {
		deltaX = 0;
	    }
	}
	int startingX = (int) getX();
	int startingY = TILE_PADDING;
	renderer.setProjectionMatrix(batch.getProjectionMatrix());
	renderer.setTransformMatrix(batch.getTransformMatrix());
	Gdx.gl.glEnable(GL20.GL_BLEND);
	Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	renderer.begin(ShapeRenderer.ShapeType.FilledRectangle);
	drawBackground(startingX, startingY);
	drawTiles(startingX, startingY);
	renderer.end();
	Gdx.gl.glDisable(GL20.GL_BLEND);
	drawItems(batch, startingX, startingY);
//	batch.draw(pixmapTexture, getX(), getY());
    }
    private final ShapeRenderer renderer = new ShapeRenderer();

    private void drawTiles(int startingX, int startingY) {
	Color tileColor = new Color(0, 0, 0, 0.7f);
	//first two lines of tiles
	//hidden if animation
	renderer.setColor(tileColor);
	if (deltaX != MAX_DELTA_X) {
	    for (int i = 0; i < 2; i++) {
		int localDeltaX = deltaX;
		if (i == 1) {
		    localDeltaX >>= 1;
		}
		for (int j = 0; j < 4; j++) {
		    renderer.filledRect(localDeltaX + startingX + i * (TILE_WIDTH + TILE_PADDING),
			    startingY + j * (TILE_HEIGHT + TILE_PADDING),
			    TILE_WIDTH, TILE_HEIGHT);
		}
	    }
	}
	//last line of tiles
	for (int i = 0; i < 6; i++) {
	    renderer.filledRect(getX() + (TILE_WIDTH + TILE_PADDING) * 2, startingY + i * (TILE_HEIGHT + TILE_PADDING),
		    TILE_WIDTH, TILE_HEIGHT);
	}
    }

    private void drawItems(SpriteBatch batch, int startingX, int startingY) {
	batch.begin();
    }

    private void drawBackground(int startingX, int startingY) {
	Color backgroundColor = new Color(1, 1, 1, 0.5f);
	renderer.setColor(backgroundColor);
	renderer.filledRect(startingX - TILE_PADDING + deltaX, startingY - TILE_PADDING,
		MAX_DELTA_X - deltaX, TILE_PADDING + (TILE_HEIGHT + TILE_PADDING) * 4);
	renderer.filledRect(getX() + (TILE_WIDTH + TILE_PADDING) * 2 - TILE_PADDING, startingY - TILE_PADDING,
		TILE_WIDTH + TILE_PADDING * 2, INVENTORY_HEIGHT);

    }
}
