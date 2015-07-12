package spaceisnear.starting.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import lombok.*;
import spaceisnear.game.ui.console.GameConsole;

public abstract class ScreenImprovedGreatly implements ScreenImproved {

    @Getter private Corev3 corev3;
    @Getter protected Stage stage;
    @Setter private Color backgroundColor = Color.BLACK;
    protected static BitmapFont font = Corev3.font;

    public OrthographicCamera getCamera() {
	return (OrthographicCamera) stage.getCamera();
    }

    public final void initialize(Corev3 corev3, Batch batch) {
	this.corev3 = corev3;
	stage = new Stage(corev3.getViewport(), batch);
	font = Corev3.font;
	create();
    }

    public void create() {
    }

    public final void setScreen(ScreenImprovedGreatly screen) {
	corev3.setScreenImproved(screen);
    }

    public GameConsole getConsole() {
	return corev3.getConsole();
    }

    @Override
    public final void render(float delta) {
	Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	draw();
	stage.draw();
	stage.act();
    }

    public void draw() {
    }

    public void update() {

    }

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
	stage.dispose();
    }

    protected void addMouseCatcher() {
	final MouseCatcher mouseCatcher = new MouseCatcher();
	mouseCatcher.setBounds(0, 0, 800, 600);
	stage.addActor(mouseCatcher);
    }
}
