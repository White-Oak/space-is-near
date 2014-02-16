package spaceisnear.starting;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import lombok.Getter;
import spaceisnear.game.Corev3;
import spaceisnear.game.ui.TextField;

/**
 *
 * @author LPzhelud
 */
public final class LoginScreen implements ScreenImproved {

    public static TextField login, password;
    private spaceisnear.game.ui.Button ok;
    @Getter private final Stage stage;
    BitmapFont font = Corev3.font;

    public LoginScreen() {
	this.stage = new Stage();
	camera = new OrthographicCamera();
	camera.setToOrtho(true);
	camera.update();
	stage.setCamera(camera);
	init();
    }

    public void init() {
	Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
	Label loginLabel = new Label("Login", labelStyle);
	Label passwordLabel = new Label("Password", labelStyle);
	login = new TextField();
	password = new TextField();

	int x = (Gdx.graphics.getWidth() - 400) >> 1;
	int y = Gdx.graphics.getHeight() >> 1;
	loginLabel.setPosition(x - passwordLabel.getWidth() - 20, y - login.getHeight());
	login.setPosition(x, y - login.getHeight() - 2);
	passwordLabel.setPosition(x - passwordLabel.getWidth() - 20, y + 10);
	password.setPosition(x, y + 8);

	ok = new spaceisnear.game.ui.Button("OK");
	ok.setPosition(x, y + password.getHeight() + 20);

	stage.addActor(loginLabel);
	stage.addActor(login);
	stage.addActor(passwordLabel);
	stage.addActor(password);
	stage.addActor(ok);
    }

    @Override
    public void render(float delta) {
	Gdx.gl.glClearColor(1, 1, 1, 1);
	Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	stage.draw();
    }
    private final OrthographicCamera camera;

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
