/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import spaceisnear.game.messages.MessageControlled;
import spaceisnear.game.layer.TiledLayer;
import spaceisnear.game.messages.MessageTimePassed;

/**
 *
 * @author LPzhelud
 */
public final class Core implements Runnable {

    private final GameContext context;
    private final GUI gui;
    private final ArrayList<GameObject> objects = new ArrayList<>();
    private final static long TWO_QUANTS_TIME = 100L;

    {
	TiledLayer tiledLayer = null;
	try {
	    tiledLayer = new TiledLayer(ImageIO.read(getClass().getResourceAsStream("/res/tiles.png")), 16, 24, 128, 128);
	    tiledLayer.fillRectTile(0, 0, 128, 128, 1);
	    tiledLayer.fillRectTile(64, 0, 64, 128, 2);
	} catch (IOException ex) {
	    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
	}
	context = new GameContext(new GameMap(tiledLayer));
	gui = new GUI(tiledLayer, context);
    }

    public void show() {
	gui.setVisible(true);
	new Thread(this).start();
	new Thread(gui).start();
    }

    @Override
    public void run() {
	try {
	    while (true) {
		MessageControlled mc = checkKeys();
		MessageTimePassed messageTimePassed = new MessageTimePassed(2);
		for (GameObject gameObject : objects) {
		    if (mc != null) {
			gameObject.message(mc);
		    }
		    gameObject.message(messageTimePassed);
		    gameObject.process();
		}
		//1 quant of time is 50L by default
		Thread.sleep(TWO_QUANTS_TIME);
	    }
	} catch (InterruptedException ex) {
	    Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    private MessageControlled checkKeys() {
	int key = gui.getKey();
	MessageControlled.Type type = null;
	switch (key) {
	    case KeyEvent.VK_UP:
		type = MessageControlled.Type.UP;
		break;
	    case KeyEvent.VK_DOWN:
		type = MessageControlled.Type.DOWN;
		break;
	    case KeyEvent.VK_LEFT:
		type = MessageControlled.Type.LEFT;
		break;
	    case KeyEvent.VK_RIGHT:
		type = MessageControlled.Type.RIGHT;
		break;
	}
	if (type == null) {
	    return null;
	}
	return new MessageControlled(type);
    }
}
