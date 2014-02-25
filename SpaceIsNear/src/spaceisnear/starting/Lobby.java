package spaceisnear.starting;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import spaceisnear.game.Corev3;
import spaceisnear.game.messages.service.onceused.MessagePlayerInformation;
import spaceisnear.game.ui.*;
import spaceisnear.starting.ui.ScreenImprovedGreatly;

/**
 *
 * @author White Oak
 */
public class Lobby extends ScreenImprovedGreatly implements ActivationListener {

    private TextField nickname;
    private Button join;

    public Lobby(Corev3 corev3) {
	super(corev3);
	init();
    }

    private void init() {
	Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
	Label loginLabel = new Label("Nickname", labelStyle);
	nickname = new TextField("kek");
	join = new Button("Join!");

	int x = (Gdx.graphics.getWidth() - 400) >> 1;
	int y = (int) (Gdx.graphics.getHeight() - 20 - nickname.getHeight() * 2 - join.getHeight());
	loginLabel.setPosition(x - loginLabel.getWidth() - 20, y);

	nickname.setPosition(x, y);
	nickname.setActivationListener(this);

	join.setPosition(x, y + nickname.getHeight() + 10);
	join.setActivationListener(this);

	addMouseCatcher();
	stage.addActor(loginLabel);
	stage.addActor(join);
	stage.addActor(nickname);

	setBackgroundColor(Color.WHITE);
    }

    @Override
    public void componentActivated(Actor actor) {
	if (actor == join) {
	    send(new MessagePlayerInformation(nickname.getText()));
	    setScreen(2);
	}
    }

}
