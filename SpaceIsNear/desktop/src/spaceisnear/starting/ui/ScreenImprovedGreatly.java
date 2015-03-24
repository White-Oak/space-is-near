package spaceisnear.starting.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import lombok.*;
import spaceisnear.game.Corev3;
import spaceisnear.game.messages.NetworkableMessage;
import spaceisnear.game.ui.console.GameConsole;

public abstract class ScreenImprovedGreatly implements ScreenImproved {

    @Getter(AccessLevel.PROTECTED) private final Corev3 corev3;
    @Getter protected final Stage stage;
    @Setter private Color backgroundColor = Color.BLACK;
    protected static BitmapFont font = Corev3.font;

    public void setCamera(Camera camera) {
	stage.getViewport().setCamera(camera);
    }

    public ScreenImprovedGreatly(Corev3 corev3) {
	this.corev3 = corev3;
	stage = new Stage(new ExtendViewport(1200, 600));
	((OrthographicCamera) stage.getCamera()).setToOrtho(true);
    }

    public final void setScreen(ScreenImprovedGreatly screen) {
	corev3.setScreenImproved(screen);
    }

    public void setScreen(int number) {
	corev3.setScreen(number);
    }

    public GameConsole getConsole() {
	return corev3.getConsole();
    }

    public void send(NetworkableMessage m) {
	corev3.send(m);
    }

    public boolean isCoreNotPaused() {
	return corev3.isCoreNotPaused();
    }

    public boolean isConnected() {
	return corev3.isConnected();
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

    protected void addMouseCatcher() {
	final MouseCatcher mouseCatcher = new MouseCatcher();
	mouseCatcher.setBounds(0, 0, 800, 600);
	stage.addActor(mouseCatcher);
    }
}