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

    @Getter private final Texture sprites;

    public Editor() {
	sprites = new Texture(Gdx.files.classpath("res").child("sprites.png"));
	camera = new OrthographicCamera();
	camera.setToOrtho(true);
	batch = new SpriteBatch();
	rightTab = new RightTab();
	itemAdder = new ItemRenderer(rightTab);
	itemAdder.setSize(Gdx.graphics.getWidth() - RightTab.TAB_WIDTH, Gdx.graphics.getHeight());
	rightTab.setPosition(Gdx.graphics.getWidth() - RightTab.TAB_WIDTH, 0);
	rightTab.setSize(Gdx.graphics.getWidth() - rightTab.getX(), Gdx.graphics.getHeight());
	rightTab.setColor(new Color(0.7f, 0.7f, 0.7f, 1f));
	stage.addActor(itemAdder);
	stage.addActor(rightTab);
	stage.setCamera(camera);
	stage.setKeyboardFocus(itemAdder);
	Gdx.input.setInputProcessor(stage);
    }
    private final RightTab rightTab;
    private final ItemRenderer itemAdder;
    private int posX, posY;

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
