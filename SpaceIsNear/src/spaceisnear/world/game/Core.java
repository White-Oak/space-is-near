/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.world.game;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import spaceisnear.world.layer.TiledLayer;

/**
 *
 * @author LPzhelud
 */
public final class Core implements Runnable {

    private final GameContext context;
    private final GUI gui;
    private final ArrayList<GameObject> objects = new ArrayList<>();

    {
	TiledLayer tiledLayer = null;
//	try {
//	    tiledLayer = new TiledLayer(ImageIO.read(getClass().getResourceAsStream("/res/tiles.png")), 50, 50, 128, 128);
//	} catch (IOException ex) {
////	    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
//	    ex.printStackTrace();
//	}
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
		for (GameObject gameObject : objects) {
		    gameObject.process(2);
		}
		Thread.sleep(100L);
	    }
	} catch (InterruptedException ex) {
	    Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
}
