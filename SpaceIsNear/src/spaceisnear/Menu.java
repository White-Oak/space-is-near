/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import spaceisnear.game.Corev2;
import spaceisnear.server.ServerCore;

/**
 *
 * @author LPzhelud
 */
public class Menu implements Screen {

    private int state = 0;
    private boolean hostresult;
    public static TextField ip;
    public static TextField nickname;
    private final Stage stage;
    Table table;
    BitmapFont font;

    public Menu() {
	this.stage = new Stage();
	font = new BitmapFont(Gdx.files.classpath("default.fnt"), false);
	final Skin skin = new Skin(Gdx.files.classpath("uiskin.json"));
	skin.add("default-font", font);
	Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.YELLOW);
	Label hostLabel = new Label("1. Host", labelStyle);
	Label connectLabel = new Label("2. Connect", labelStyle);
	ip = new TextField("lol", skin);
	table = new Table();
	table.setFillParent(true);
	table.add(hostLabel);
	table.row();
	table.add(connectLabel);
	stage.addActor(table);
	camera = new OrthographicCamera();
	camera.setToOrtho(true);
	camera.update();
    }

    public void update() {
	if (state == 3) {
	    if (hostresult) {
		try {
		    ServerCore serverCore = new ServerCore();
		    try {
			serverCore.host();
		    } catch (IOException ex) {
			Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
		    }
		    new Thread(serverCore).start();
		    Corev2.IP = "127.0.0.1";
		    System.out.println("Hosting...");
		} catch (IOException ex) {
		    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
		}
	    }
	    //вырезаем всё, кроме цифр и точек. бизапаснасть if (Corev2.IP == null) { Corev2.IP = ip.getText().replaceAll("[^0-9^/.]", ""); }
	    System.out.println("connecting to " + Corev2.IP + "...");
	    Main.main.setScreen(Main.main.screen);
	}

    }

    /**
     * public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException { g.setColor(Color.black);
     * g.fillRect(0, 0, game.getContainer().getWidth(), game.getContainer().getHeight()); switch (state) { case 0: g.setColor(Color.yellow);
     * g.drawString("Enter your nickname:", (game.getContainer().getWidth() >> 1) - (g.getFont().getWidth("Enter your nickname:") >> 1),
     * (game.getContainer().getHeight() >> 1) - 100); nickname.render(container, g); break; case 1: g.setColor(Color.yellow);
     * g.drawString("1. host\n2. join", (game.getContainer().getWidth() >> 1) - (g.getFont().getWidth( "1. host\n2. join") >> 1),
     * (game.getContainer().getHeight() >> 1) - 100); break; case 2: //не придумал, как покрасивее сделать, поэтому сделал тупо смещение
     * g.setColor(Color.yellow); g.drawString("1. host\n 2. join\n\nenter ip address:", (game.getContainer().getWidth() >> 1) -
     * (g.getFont().getWidth("1. host\n 2. join\n\nenter ip address:") >> 1), (game.getContainer().getHeight() >> 1) - 100);
     * ip.render(container, g); break; } }
     *
     * public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException { if (state == 3) { if (hostresult)
     * { try { ServerCore serverCore = new ServerCore(); try { serverCore.host(); } catch (IOException ex) {
     * Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex); } new Thread(serverCore).start(); Corev2.IP = "127.0.0.1";
     * System.out.println("Hosting..."); } catch (IOException ex) { Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex); } }
     * //вырезаем всё, кроме цифр и точек. бизапаснасть if (Corev2.IP == null) { Corev2.IP = ip.getText().replaceAll("[^0-9^/.]", ""); }
     * System.out.println("connecting to " + Corev2.IP + "..."); game.enterState(2, new FadeOutTransition(Color.white, 400), new
     * FadeInTransition(Color.white, 400)); }
     *
     * }
     * //Хз, что хотел реализовать //я тоже не знаю
     *
     * public void keyPressed(int key, char c) { switch (state) { case 1: switch (c) { case '1': hostresult = true; state = 3; break; case
     * '2': state++; break; } break; } }
     *
     * public void componentActivated(AbstractComponent ac) { System.out.println("true"); if (ac == (ip)) { state = 3; hostresult = false;
     * ip.setFocus(false); } else if (ac.equals(nickname)) { state = 1; nickname.setFocus(false); } }
     */
    @Override
    public void render(float delta) {
	Gdx.gl.glClearColor(0, 0, 0, 1);
	Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	stage.draw();
	if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
	    state = 3;
	    hostresult = true;
	    update();
	}
	SpriteBatch batch = new SpriteBatch();
	batch.setProjectionMatrix(camera.combined);
	batch.begin();
	font.draw(batch, "Lol noobs", 0, 0);
	batch.end();
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
