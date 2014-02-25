package spaceisnear.starting;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import spaceisnear.game.Corev3;
import spaceisnear.game.messages.service.onceused.MessageClientInformation;
import spaceisnear.game.ui.ActivationListener;
import spaceisnear.game.ui.TextField;
import spaceisnear.starting.ui.ScreenImprovedGreatly;

/**
 *
 * @author LPzhelud
 */
public final class LoginScreen extends ScreenImprovedGreatly implements ActivationListener {

    public static TextField login, password;
    private spaceisnear.game.ui.Button ok;

    public LoginScreen(Corev3 corev3) {
	super(corev3);
	init();
    }

    public void init() {
	Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
	Label loginLabel = new Label("Login", labelStyle);
	Label passwordLabel = new Label("Password", labelStyle);
	login = new TextField("true");
	password = new TextField("lel");

	int x = (Gdx.graphics.getWidth() - 400) >> 1;
	int y = Gdx.graphics.getHeight() >> 1;
	loginLabel.setPosition(x - passwordLabel.getWidth() - 20, y - login.getHeight());
	login.setPosition(x, y - login.getHeight() - 2);
	passwordLabel.setPosition(x - passwordLabel.getWidth() - 20, y + 10);
	password.setPosition(x, y + 8);

	ok = new spaceisnear.game.ui.Button("OK");
	ok.setPosition(x, y + password.getHeight() + 20);
	ok.setActivationListener(this);

	addMouseCatcher();

	stage.addActor(loginLabel);
	stage.addActor(login);
	stage.addActor(passwordLabel);
	stage.addActor(password);
	stage.addActor(ok);

	setBackgroundColor(Color.WHITE);
    }

    @Override
    public void componentActivated(Actor actor) {
	if (isConnected() && actor == ok) {
	    send(new MessageClientInformation(login.getText(), password.getText()));
	    setScreen(1);
	}
    }
}
