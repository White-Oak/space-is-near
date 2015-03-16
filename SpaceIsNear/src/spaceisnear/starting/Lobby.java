package spaceisnear.starting;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import java.util.Random;
import spaceisnear.game.Corev3;
import spaceisnear.game.messages.service.onceused.MessagePlayerInformation;
import spaceisnear.game.ui.*;
import spaceisnear.starting.ui.ScreenImprovedGreatly;

/**
 *
 * @author White Oak
 */
public class Lobby extends ScreenImprovedGreatly implements ActivationListener {

    private TextField nickname, profession;
    private Button join;

    public Lobby(Corev3 corev3) {
	super(corev3);
	init();
    }

    private void init() {
	Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
	Label loginLabel = new Label("Nickname", labelStyle);
	Label proLabel = new Label("Profession", labelStyle);
	nickname = new TextField(getRandomName());
	profession = new TextField("CCleaner");
	join = new Button("Join!");

	int x = (Gdx.graphics.getWidth() - 400) >> 1;
	int y = (int) (Gdx.graphics.getHeight() - 20 - nickname.getHeight() * 2 - join.getHeight());
	loginLabel.setPosition(x - proLabel.getWidth() - 20, y);
	proLabel.setPosition(x - proLabel.getWidth() - 20, y - profession.getHeight());

	nickname.setPosition(x, y);

	profession.setPosition(x, y - profession.getHeight());

	join.setPosition(x, y + nickname.getHeight() + 10);
	join.setActivationListener(this);

	addMouseCatcher();
	stage.addActor(loginLabel);
	stage.addActor(join);
	stage.addActor(nickname);
	stage.addActor(proLabel);
	stage.addActor(profession);

	setBackgroundColor(Color.WHITE);
    }

    @Override
    public void componentActivated(UIElement actor) {
	if (actor == join) {
	    send(new MessagePlayerInformation(nickname.getText(), profession.getText()));
	}
    }

    /**
     * it's fuken purrfect. tis a flower m8. tis a rainbow. and a river. and a sun.
     *
     * @author riseremi
     */
    private String getRandomName() {
	String[] names = {"Aaron", "Abbey", "Acacia", "Adam", "Aden", "Adolph",
			  "Alexia", "Alf", "Alexandria", "Amber", "Azura", "Antonio", "Amadeus"};
	String[] surnames = {"Setters", "Shann", "Shaw", "Shield", "Settle",
			     "Shady", "Share", "Shark", "Shill", "Sherwin", "Vivaldi", "Mozart"};
	Random rnd = new Random();
	return names[rnd.nextInt(names.length)] + " " + surnames[rnd.nextInt(surnames.length)];
    }
}
