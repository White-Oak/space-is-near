package spaceisnear.starting;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import lombok.Setter;
import me.whiteoak.minlog.Log;
import static spaceisnear.game.Corev2.rayHandler;
import static spaceisnear.game.Corev2.world;
import spaceisnear.game.Networking;
import spaceisnear.game.messages.service.onceused.MessageLogin;
import spaceisnear.game.objects.items.ItemsArchive;
import spaceisnear.game.objects.items.ItemsReader;
import spaceisnear.game.ui.*;
import spaceisnear.game.ui.console.ChatString;
import spaceisnear.game.ui.console.LogLevel;
import spaceisnear.starting.ui.ScreenImprovedGreatly;

/**
 *
 * @author LPzhelud
 */
public final class LoginScreen extends ScreenImprovedGreatly implements ActivationListener {

    private TextField login, password;
    private spaceisnear.game.ui.Button ok;
    @Setter private Future<Networking> networking;

    @Override
    public void create() {
	world = new World(new Vector2(), true);
	rayHandler = new RayHandler(world);
	RayHandler.useDiffuseLight(true);
	rayHandler.setCulling(true);
	rayHandler.setBlur(true);
	initView();
	try {
	    ItemsArchive.itemsArchive = new ItemsArchive(ItemsReader.read());
	} catch (Exception ex) {
	    Log.error("client", "While trying to create ItemsArchive", ex);
	    System.exit(1);
	}
    }

    private void initView() {
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

//	addMouseCatcher();
	stage.addActor(loginLabel);
	stage.addActor(login);
	stage.addActor(passwordLabel);
	stage.addActor(password);
	stage.addActor(ok);

	setBackgroundColor(new Color(0xecf0f1ff));
    }

    @Override
    public void componentActivated(UIElement actor) {
	if (isConnected()) {
	    if (actor == ok) {
		try {
		    networking.get().send(new MessageLogin(login.getText(), password.getText()));
		} catch (InterruptedException | ExecutionException ex) {
		    Log.error("client", "While trying to connect to server", ex);
		}
	    }
	} else {
	    getConsole().pushMessage(new ChatString("Wait for the connection to be established!", LogLevel.WARNING));
	}
    }

    private boolean isConnected() {
	return networking != null && networking.isDone();
    }

    @Override
    public void update() {
	if (isConnected()) {
	    try {
		if (networking.get().isLogined()) {
		    Lobby lobby = new Lobby(networking.get());
		    setScreen(lobby);
		}
	    } catch (InterruptedException | ExecutionException ex) {
		Log.error("client", "While trying to switch to lobby screen", ex);
	    }
	}
    }

}
