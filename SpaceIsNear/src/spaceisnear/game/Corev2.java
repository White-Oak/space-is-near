/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import spaceisnear.game.components.PaintableComponent;
import spaceisnear.game.components.PositionComponent;
import spaceisnear.game.layer.TiledLayer;
import spaceisnear.game.messages.MessageControlled;
import spaceisnear.game.messages.MessageTimePassed;

/**
 *
 * @author LPzhelud
 */
public class Corev2 extends BasicGame {

    private GameContext context;
    private final ArrayList<GameObject> objects = new ArrayList<>();
    private final static int QUANT_TIME = 50;
    private int key;

    public Corev2(String title) {
	super(title);
    }

    @Override
    public void init(GameContainer container) throws SlickException {
	TiledLayer tiledLayer = null;
	try {
	    tiledLayer = new TiledLayer(new PositionComponent(),
					new Image(getClass().getResourceAsStream("/res/tiles.png"), "sprites", false), GameContext.TILE_WIDTH,
					GameContext.TILE_HEIGHT, 128, 128);
	    tiledLayer.fillRectTile(0, 0, 128, 128, 1);
	    tiledLayer.fillRectTile(64, 0, 64, 128, 2);
	} catch (Exception ex) {
	    Logger.getLogger(Corev2.class.getName()).log(Level.SEVERE, null, ex);
	}
	context = new GameContext(new CameraMan(tiledLayer));
	GamerPlayer player = new GamerPlayer(1, null, context);
	objects.add(player);
	context.getCamera().setWindowWidth(800);
	context.getCamera().setWindowHeight(600);

    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
	MessageControlled mc = checkKeys();
	//1 quant of time is 50L by default
	int quants = delta / QUANT_TIME;
	MessageTimePassed messageTimePassed = new MessageTimePassed(quants);
	for (GameObject gameObject : objects) {
	    if (mc != null) {
		gameObject.message(mc);
	    }
	    gameObject.message(messageTimePassed);
	    gameObject.process();
	}

    }

    private MessageControlled checkKeys() {
	MessageControlled mc = null;
	switch (key) {
	    case Input.KEY_UP:
		mc = new MessageControlled(MessageControlled.Type.UP);
		break;
	    case Input.KEY_DOWN:
		mc = new MessageControlled(MessageControlled.Type.DOWN);
		break;
	    case Input.KEY_LEFT:
		mc = new MessageControlled(MessageControlled.Type.LEFT);
		break;
	    case Input.KEY_RIGHT:
		mc = new MessageControlled(MessageControlled.Type.RIGHT);
		break;
	}
	return mc;
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
	context.getCamera().moveCamera(g);
	context.getCamera().paint(g);
	for (PaintableComponent paintableComponent : context.getPaintables()) {
	    paintableComponent.paint(g);
	}
	context.getCamera().unmoveCamera(g);
    }

    @Override
    public void keyPressed(int key, char c) {
	this.key = key;
    }

    @Override
    public void keyReleased(int key, char c) {
	this.key = 0;
    }
}
