/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.*;
import lombok.Getter;
import spaceisnear.game.GameContext;

/**
 *
 * @author White Oak
 */
public class ItemRenderer extends Actor {

    final ItemsHandler handler = ItemsHandler.HANDLER;
    @Getter private MapAction.Type mode = MapAction.Type.ADD;
    private final OrthographicCamera camera;

    public ItemRenderer(final RightTab tab) {
	addCaptureListener(new InputListener() {
	    private int pressedX, pressedY;
	    private int currentX, currentY;

	    @Override
	    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		x -= posX;
		y -= posY;
		x /= GameContext.TILE_WIDTH;
		y /= GameContext.TILE_HEIGHT;
		pressedX = (int) x;
		pressedY = (int) y;
		currentX = pressedX;
		currentY = pressedY;
		MapAction mapAction = new MapAction(mode, new Item(tab.getChosenOne(), pressedX, pressedY));
		if (mode == MapAction.Type.FILL) {
		    mapAction.setFillX(currentX);
		    mapAction.setFillY(currentY);
		}
		handler.setCurrentAction(mapAction);
		return true;
	    }

	    @Override
	    public void touchDragged(InputEvent event, float x, float y, int pointer) {
		x -= posX;
		y -= posY;
		x /= GameContext.TILE_WIDTH;
		y /= GameContext.TILE_HEIGHT;
		currentX = (int) x;
		currentY = (int) y;
		switch (mode) {
		    case FILL:
			handler.getCurrentAction().setFillX(currentX);
			handler.getCurrentAction().setFillY(currentY);
			break;
		}
	    }

	    @Override
	    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
		handler.addCurrentAction();
	    }

	    @Override
	    public boolean keyDown(InputEvent event, int keycode) {
		switch (keycode) {
		    case Input.Keys.A:
			mode = MapAction.Type.ADD;
			break;
		    case Input.Keys.D:
			mode = MapAction.Type.DELETE;
			break;
		    case Input.Keys.F:
			mode = MapAction.Type.FILL;
			break;
		    case Input.Keys.S:
			handler.save();
			break;
		    case Input.Keys.L:
			handler.load();
			break;
		    case Input.Keys.E:
			Gdx.app.exit();
			break;
		}
		return true;
	    }

	});
	camera = new OrthographicCamera(getWidth(), getHeight());
	camera.setToOrtho(true);
    }

    public void move(int x, int y) {
//	camera.translate(x, y);
//	camera.update();
	posX += x;
	posY += y;
    }
    private int posX, posY;

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
//	batch.setProjectionMatrix(camera.combined);
//	Rectangle scissors = new Rectangle();
//	Rectangle clipBounds = new Rectangle(-1, -1, getWidth(), getHeight());
//	ScissorStack.calculateScissors(camera, batch.getTransformMatrix(), clipBounds, scissors);
//	ScissorStack.pushScissors(scissors);
	handler.getItems().forEach((item) -> {
	    final int x = item.getX() * GameContext.TILE_WIDTH + posX;
	    final int y = item.getY() * GameContext.TILE_HEIGHT + posY;
	    if (x > -GameContext.TILE_WIDTH && x < getWidth() && y > -GameContext.TILE_HEIGHT && y < getHeight()) {
		batch.draw(handler.getTextureRegion(item.getId()), x, y);
	    }
	});
	if (handler.getCurrentAction() != null) {
	    handler.getCurrentAction().draw(batch, posX, posY);
	}
	renderer.setProjectionMatrix(batch.getProjectionMatrix());
	renderer.setTransformMatrix(batch.getTransformMatrix());
	renderer.begin(ShapeRenderer.ShapeType.Rectangle);
	renderer.setColor(Color.RED);
	renderer.rect(posX, posY, 64 * GameContext.TILE_WIDTH, 64 * GameContext.TILE_HEIGHT);
	renderer.end();
//	ScissorStack.popScissors();
    }
    private final ShapeRenderer renderer = new ShapeRenderer();

}
