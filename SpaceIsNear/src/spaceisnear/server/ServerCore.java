/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.server;

import lombok.AccessLevel;
import lombok.Getter;
import org.newdawn.slick.Image;
import spaceisnear.game.layer.TiledLayer;
import spaceisnear.server.objects.GameObject;
import spaceisnear.server.objects.Player;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author white_oak
 */
public class ServerCore {

    @Getter(AccessLevel.PRIVATE) private GameContext context;
    private boolean unbreakable = true;
    private boolean paused = false;
    private final static int QUANT_TIME = 20;
    private boolean alreadyPaused;

    public ServerCore() {
	TiledLayer tiledLayer = null;
	//<editor-fold defaultstate="collapsed" desc="map generating">
	try {
	    tiledLayer = new TiledLayer(new Image(getClass().getResourceAsStream("/res/tiles1.png"), "sprites", false),
		    spaceisnear.game.GameContext.TILE_WIDTH,
		    spaceisnear.game.GameContext.TILE_HEIGHT, 2048, 2048);
	    //tiledLayer.fillRectTile(0, 0, 128, 128, 1);
	    //tiledLayer.fillRectTile(64, 0, 64, 128, 2);

	    //карту рисуем, чо. пока так
	    //тут заливка травой всей карты
	    tiledLayer.fillRectTile(0, 0, 2048, 2048, 5);

	    //генерируем по 12500 тайлов с землёй для более КРАСИВОГО ПЕЙЗАЖА
	    Random rnd = new Random();
	    //тут сделаем 200 "островков" с землёй по четыре тайла для ещё более КРАСИВОГО ПЕЙЗАЖА
	    for (int i = 0; i < 200; i++) {
		int blockx = rnd.nextInt(124);
		int blocky = rnd.nextInt(124);

		tiledLayer.setTile(blockx, blocky, 7);
		tiledLayer.setTile(blockx + 1, blocky, 8);
		tiledLayer.setTile(blockx, blocky + 1, 9);
		tiledLayer.setTile(blockx + 1, blocky + 1, 10);
	    }
	} catch (Exception ex) {
	    Logger.getLogger(ServerCore.class.getName()).log(Level.SEVERE, null, ex);
	}
	//</editor-fold>
	context = new GameContext(new Networking(this), new ArrayList<GameObject>());
    }

    public void run() {
	while (unbreakable) {
	    if (paused) {
		for (Iterator<GameObject> it = getContext().getObjects().iterator(); it.hasNext();) {
		    GameObject gameObject = it.next();
		    gameObject.process();
		}
	    } else {
		alreadyPaused = true;
	    }
	}
    }

    public void pause() {
	paused = true;
    }

    public void unpause() {
	paused = false;
	alreadyPaused = false;
    }

    public Player addPlayer(int connectionID) {
	return context.addPlayer(connectionID);
    }

    public boolean isAlreadyPaused() {
	return alreadyPaused;
    }
}
