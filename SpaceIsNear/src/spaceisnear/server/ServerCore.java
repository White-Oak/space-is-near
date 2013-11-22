/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.server;

import java.io.IOException;
import lombok.AccessLevel;
import lombok.Getter;
import org.newdawn.slick.Image;
import spaceisnear.game.layer.TiledLayer;
import spaceisnear.server.objects.GameObject;
import spaceisnear.server.objects.Player;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import spaceisnear.game.messages.MessagePaused;
import spaceisnear.game.messages.MessageUnpaused;

/**
 * @author white_oak
 */
public class ServerCore implements Runnable {

    @Getter(AccessLevel.PACKAGE) private GameContext context;
    private boolean unbreakable = true;
    private boolean paused = false;
    private final static int QUANT_TIME = 20;
    @Getter private boolean alreadyPaused;
    @Getter private TiledLayer tiledLayer;

    public ServerCore() {
	//<editor-fold defaultstate="collapsed" desc="map generating">
	try {
	    tiledLayer = new TiledLayer(new Image(getClass().getResourceAsStream("/res/tiles1.png"), "sprites", false),
		    spaceisnear.game.GameContext.TILE_WIDTH,
		    spaceisnear.game.GameContext.TILE_HEIGHT, 128, 128);
	    //tiledLayer.fillRectTile(0, 0, 128, 128, 1);
	    //tiledLayer.fillRectTile(64, 0, 64, 128, 2);

	    //карту рисуем, чо. пока так
	    //тут заливка травой всей карты
	    tiledLayer.fillRectTile(0, 0, 128, 128, 5);

	    //генерируем по 12500 тайлов с землёй для более КРАСИВОГО ПЕЙЗАЖА
	    Random rnd = new Random();
	    //тут сделаем 200 "островков" с землёй по четыре тайла для ещё более КРАСИВОГО ПЕЙЗАЖА
	    for (int i = 0; i < 5; i++) {
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

    @Override
    public void run() {
	while (unbreakable) {
	    if (!paused) {
		for (Iterator<GameObject> it = getContext().getObjects().iterator(); it.hasNext();) {
		    GameObject gameObject = it.next();
		    gameObject.process();
		}
	    } else {
		if (!alreadyPaused) {
		    alreadyPaused = true;
		}
	    }
	    try {
		Thread.sleep(QUANT_TIME);
	    } catch (InterruptedException ex) {
		Logger.getLogger(ServerCore.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
    }

    public void pause() {
	paused = true;
	context.getNetworking().sendToAll(new MessagePaused());
    }

    public void unpause() {
	context.getNetworking().sendToAll(new MessageUnpaused());
	paused = false;
	alreadyPaused = false;
    }

    public void host() throws IOException {
	getContext().getNetworking().host();
    }

    public Player addPlayer(int connectionID) {
	return context.addPlayer(connectionID);
    }
}
