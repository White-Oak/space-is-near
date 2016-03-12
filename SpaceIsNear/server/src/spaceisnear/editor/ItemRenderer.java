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
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.*;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import spaceisnear.abstracts.Context;
import spaceisnear.game.ui.context.ContextMenu;

/**
 *
 * @author White Oak
 */
public class ItemRenderer extends Actor {

    final ItemsHandler handler = ItemsHandler.HANDLER;
    @Getter @Setter private MapAction.Type mode = MapAction.Type.ADD;
    private final OrthographicCamera camera;
    private boolean blocked;
    private PropertiesWindow propertiesWindow;
    @Getter private int currentX, currentY;

    private MapAction setCurrentActionWith(Item item) {
	MapAction mapAction = new MapAction(mode, item);
	handler.setCurrentAction(mapAction);
	return mapAction;
    }

    public ItemRenderer(final RightTab tab) {
	addCaptureListener(new InputListener() {
	    private int pressedX, pressedY;

	    private int getTilesX(float realx) {
		realx -= posX;
		return (int) realx / Context.TILE_WIDTH;
	    }

	    private int getTilesY(float realy) {
		realy -= posY;
		return (int) realy / Context.TILE_HEIGHT;
	    }

	    @Override
	    public boolean mouseMoved(InputEvent event, float x, float y) {
		currentX = getTilesX(x);
		currentY = getTilesY(y);
		return true;
	    }

	    @Override
	    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		if (!blocked) {
		    pressedX = getTilesX(x);
		    pressedY = getTilesY(y);
		    currentX = pressedX;
		    currentY = pressedY;
//		    System.out.println("x " + pressedX + " y " + pressedY);
		    switch (mode) {
			case ADD:
			case FILL: {
			    MapAction mapAction = setCurrentActionWith(new Item(tab.getChosenOne(), pressedX, pressedY));
			    if (mode == MapAction.Type.FILL) {
				mapAction.setFillX(currentX);
				mapAction.setFillY(currentY);
			    }
			}
			break;
			case DELETE:
			case PROPERTIES: {
			    blocked = true;
			    ArrayList<Item> items = handler.getItems();
			    ArrayList<Item> itemsMenu = new ArrayList<>();
			    items.stream()
				    .filter(item -> (item.getX() == currentX && item.getY() == currentY))
				    .forEach(item -> itemsMenu.add(item));
			    if (!itemsMenu.isEmpty()) {
				ContextMenu contextMenu = new ContextMenu("", getStage(), (int) x, (int) y);
				itemsMenu.forEach(item -> contextMenu.add(handler.getName(item.getId())));
				contextMenu.setActivationListener(actor -> {
				    int selected = contextMenu.getSelected();
				    Item item = itemsMenu.get(selected);
				    setCurrentActionWith(item);
				    handler.addCurrentAction();
				    contextMenu.hide();
				    blocked = false;
				});
				contextMenu.show();
			    } else {
				blocked = false;
			    }
			}
			break;
		    }
		}
		return true;
	    }

	    @Override
	    public void touchDragged(InputEvent event, float x, float y, int pointer) {
		if (!blocked) {
		    currentX = getTilesX(x);
		    currentY = getTilesY(y);
		    switch (mode) {
			case FILL:
			    handler.getCurrentAction().setFillX(currentX);
			    handler.getCurrentAction().setFillY(currentY);
			    break;
		    }
		}
	    }

	    @Override
	    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
		if (!blocked) {
		    pressedX = getTilesX(x);
		    pressedY = getTilesY(y);
		    if (pressedX == currentX && pressedY == currentY) {
			handler.addCurrentAction();
		    }
		}
	    }

	    @Override
	    public boolean keyDown(InputEvent event, int keycode) {
		if (!blocked) {
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
			case Input.Keys.P:
			    mode = MapAction.Type.PROPERTIES;
			    break;
		    }

		}
		return true;
	    }

	}
	);
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
    public void draw(Batch batch, float parentAlpha) {
//	batch.setProjectionMatrix(camera.combined);
//	Rectangle scissors = new Rectangle();
//	Rectangle clipBounds = new Rectangle(-1, -1, getWidth(), getHeight());
//	ScissorStack.calculateScissors(camera, batch.getTransformMatrix(), clipBounds, scissors);
//	ScissorStack.pushScissors(scissors);
	handler.getItems().forEach((item) -> {
	    final int x = item.getX() * Context.TILE_WIDTH + posX;
	    final int y = (int) (item.getY() * Context.TILE_HEIGHT + posY + getY());
	    if (x > -Context.TILE_WIDTH && x < getWidth() && y > -Context.TILE_HEIGHT && y < getHeight()) {
		batch.draw(handler.getTextureRegion(item.getId()), x, y);
	    }
	});
	if (handler.getCurrentAction() != null) {
	    handler.getCurrentAction().draw(batch, posX, (int) (posY + getY()));
	}
	renderer.setProjectionMatrix(batch.getProjectionMatrix());
	renderer.setTransformMatrix(batch.getTransformMatrix());
	renderer.begin(ShapeRenderer.ShapeType.Line);
	renderer.setColor(Color.RED);
	renderer.rect(posX, posY + getY(), 64 * Context.TILE_WIDTH, 64 * Context.TILE_HEIGHT);
	renderer.end();
//	ScissorStack.popScissors();
    }
    private final ShapeRenderer renderer = new ShapeRenderer();

}
