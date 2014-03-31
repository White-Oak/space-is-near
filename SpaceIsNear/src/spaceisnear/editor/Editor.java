/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.editor;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import lombok.Getter;
import spaceisnear.game.ui.MenuBar;
import spaceisnear.game.ui.MenuItem;
import spaceisnear.game.ui.context.ContextMenu;
import spaceisnear.game.ui.context.ContextMenuItem;

/**
 *
 * @author White Oak
 */
public class Editor implements Screen {

    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final FPSLogger fps = new FPSLogger();
    private final Stage stage = new Stage();
    private final ItemsHandler handler = ItemsHandler.HANDLER;
    private final MenuBar menuBar;

    @Getter private final Texture sprites;

    public Editor() {
	sprites = new Texture(Gdx.files.classpath("res").child("sprites.png"));
	camera = new OrthographicCamera();
	camera.setToOrtho(true);
	batch = new SpriteBatch();
	rightTab = new RightTab();
	rightTab.setPosition(Gdx.graphics.getWidth() - RightTab.TAB_WIDTH, 0);
	rightTab.setSize(Gdx.graphics.getWidth() - rightTab.getX(), Gdx.graphics.getHeight());
	rightTab.setColor(new Color(0.7f, 0.7f, 0.7f, 1f));

	menuBar = new MenuBar();
	initializeMenuBar();

	itemAdder = new ItemRenderer(rightTab);
	itemAdder.setBounds(0, menuBar.getHeight(), Gdx.graphics.getWidth() - RightTab.TAB_WIDTH,
		Gdx.graphics.getHeight() - menuBar.getHeight());

	stage.addActor(itemAdder);
	stage.addActor(menuBar);
	stage.addActor(rightTab);
	stage.setCamera(camera);
	stage.setKeyboardFocus(itemAdder);
	Gdx.input.setInputProcessor(stage);
    }
    private final RightTab rightTab;
    private final ItemRenderer itemAdder;
    private int posX, posY;

    private void initializeMenuBar() {
	MenuItem menuItem = new MenuItem("File", stage);
	menuItem.setActivationListener((e) -> {
	    ContextMenu item = (ContextMenu) e;
	    switch (item.getSelected()) {
		case 0:
		    handler.load();
		    break;
		case 1:
		    handler.save();
		    break;
		case 2:
		    Gdx.app.exit();
		    break;
	    }
	});
	menuItem.add(new ContextMenuItem("(L)oad"));
	menuItem.add(new ContextMenuItem("(S)ave"));
	menuItem.add(new ContextMenuItem("(E)xit"));
	menuBar.add(menuItem);
	menuItem = new MenuItem("Edit", stage);
	menuItem.setActivationListener((e) -> {
	    ContextMenu item = (ContextMenu) e;
	    switch (item.getSelected()) {
		case 0:
		    itemAdder.setMode(MapAction.Type.ADD);
		    break;
		case 1:
		    itemAdder.setMode(MapAction.Type.FILL);
		    break;
		case 2:
		    itemAdder.setMode(MapAction.Type.DELETE);
		    break;
	    }
	});
	menuItem.add(new ContextMenuItem("(A)dd mode"));
	menuItem.add(new ContextMenuItem("(F)ill mode"));
	menuItem.add(new ContextMenuItem("(D)elete mode"));
	menuBar.add(menuItem);
	menuBar.setWidth(Gdx.graphics.getWidth() - RightTab.TAB_WIDTH);
    }

    @Override
    public void render(float delta) {
	Gdx.gl.glClearColor(0, 0, 0.2f, 1);
	Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	camera.update();

//	batch.setProjectionMatrix(camera.combined);
	stage.draw();
	if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
	    itemAdder.move(0, -CAMERA_MOVE);
	}
	if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
	    itemAdder.move(0, CAMERA_MOVE);
	}
	if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
	    itemAdder.move(CAMERA_MOVE, 0);
	}
	if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
	    itemAdder.move(-CAMERA_MOVE, 0);
	}
    }
    private static final int CAMERA_MOVE = 5;

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

}
