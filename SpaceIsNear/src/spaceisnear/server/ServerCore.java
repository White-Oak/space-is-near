// Generated by delombok at Sun Dec 01 13:28:22 MSK 2013
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.server;

import java.io.IOException;
import java.io.InputStream;
import org.newdawn.slick.Image;
import spaceisnear.game.layer.TiledLayer;
import spaceisnear.server.objects.Player;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.SlickException;
import spaceisnear.AbstractGameObject;
import spaceisnear.game.GameContext;
import spaceisnear.game.components.server.HealthComponent;
import spaceisnear.game.console.LogLevel;
import spaceisnear.game.console.LogString;
import spaceisnear.game.layer.AtmosphericLayer;
import spaceisnear.game.layer.ObstaclesLayer;
import spaceisnear.game.messages.MessageDied;
import spaceisnear.game.messages.MessageKnockbacked;
import spaceisnear.game.messages.MessageLog;
import spaceisnear.game.messages.MessagePaused;
import spaceisnear.game.messages.MessageToSend;
import spaceisnear.game.messages.MessageUnpaused;
import spaceisnear.server.objects.ServerNetworkingObject;
import spaceisnear.server.objects.items.ItemAdder;

/**
 * @author white_oak
 */
public class ServerCore implements Runnable {

    private final ServerContext context;
    private final boolean unbreakable = true;
    private boolean paused = false;
    private static final long QUANT_TIME = 20;
    private boolean alreadyPaused;
    private TiledLayer tiledLayer;
    public static final int OBJECTS_TO_SKIP = 1;
    private long timePassed;
    private AtmosphereThread at = new AtmosphereThread();

    public ServerCore() throws IOException {
	int width = GameContext.MAP_WIDTH;
	int height = width;
	//<editor-fold defaultstate="collapsed" desc="map generating">
	try {
	    try (InputStream resourceAsStream = getClass().getResourceAsStream("/res/tiles1.png")) {
		tiledLayer = new TiledLayer(new Image(resourceAsStream, "tiles", false),
			spaceisnear.game.GameContext.TILE_WIDTH, spaceisnear.game.GameContext.TILE_HEIGHT, width, height);
	    }
	    //tiledLayer.fillRectTile(0, 0, 128, 128, 1);
	    //tiledLayer.fillRectTile(64, 0, 64, 128, 2);
	    tiledLayer.fillRectTile(0, 0, width, height, 5);
	    Random rnd = new Random();
	    for (int i = 0; i < 5; i++) {
		int blockx = rnd.nextInt(width - 1);
		int blocky = rnd.nextInt(height - 1);
		tiledLayer.setTile(blockx, blocky, 7);
		tiledLayer.setTile(blockx + 1, blocky, 8);
		tiledLayer.setTile(blockx, blocky + 1, 9);
		tiledLayer.setTile(blockx + 1, blocky + 1, 10);
	    }
	} catch (SlickException | IOException ex) {
	    Logger.getLogger(ServerCore.class.getName()).log(Level.SEVERE, null, ex);
	}
	//</editor-fold>
	final ArrayList<AbstractGameObject> objects = new ArrayList<>();
	ObstaclesLayer obstacles = new ObstaclesLayer(width, height);
	AtmosphericLayer atmosphere = new AtmosphericLayer(width, height);
	context = new ServerContext(new ServerNetworking(this), objects, tiledLayer, obstacles, atmosphere);
	//ui objects etc
	while (objects.size() < OBJECTS_TO_SKIP) {
	    objects.add(null);
	}
	//items adding
	ItemAdder itemAdder = new ItemAdder(context);
	itemAdder.addItems();
    }

    @Override
    public void run() {
	at.start();
	while (unbreakable) {
	    if (!paused) {
		for (AbstractGameObject gameObject : getContext().getObjects()) {
		    if (gameObject != null) {
			gameObject.process();
		    }
		}
		checkHealthStatuses();
	    } else {
		if (!alreadyPaused) {
		    alreadyPaused = true;
		}
	    }
	    try {
		Thread.sleep(QUANT_TIME);
		timePassed += QUANT_TIME;
		if (timePassed > 2000 && !context.getPlayers().isEmpty()) {
		    Player get = context.getPlayers().get(0);
		    final int pressure = context.getAtmosphere().getPressure(get.getPosition().getX(), get.getPosition().getY());
		    context.getNetworking().sendToAll(new MessageLog(new LogString("Pressure: " + pressure, LogLevel.DEBUG)));
		    timePassed = 0;
		}
	    } catch (InterruptedException ex) {
		Logger.getLogger(ServerCore.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
    }

    private void checkHealthStatuses() {
	for (Player player : getContext().getPlayers()) {
	    HealthComponent hc = player.getHealthComponent();
	    switch (hc.getState()) {
		case CRITICICAL:
		    MessageKnockbacked messageKnockbacked = new MessageKnockbacked(player.getId());
		    getContext().sendToID(messageKnockbacked, player.getId());
		    getContext().sendToID(new MessageToSend(messageKnockbacked), player.getId());
		    break;

		case DEAD:
		    MessageDied messageDied = new MessageDied(player.getId());
		    getContext().sendToID(messageDied, player.getId());
		    getContext().sendToID(new MessageToSend(messageDied), player.getId());
		    break;

	    }
	}
    }

    public void pause() {
	paused = true;
	context.getNetworking().sendToAll(new MessagePaused());
	System.out.println("Server\'s been paused");
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

    ServerContext getContext() {
	return this.context;
    }

    public boolean isAlreadyPaused() {
	return this.alreadyPaused;
    }

    public TiledLayer getTiledLayer() {
	return this.tiledLayer;
    }

    private class AtmosphereThread extends Thread {

	@Override
	public void run() {
	    while (unbreakable) {
		if (!paused) {
		    context.getAtmosphere().tickAtmosphere(context);
		}
		try {
		    Thread.sleep(ATMOSPHERE_DELAY);
		} catch (InterruptedException ex) {
		    Logger.getLogger(ServerCore.class.getName()).log(Level.SEVERE, null, ex);
		}
	    }
	}
	public static final long ATMOSPHERE_DELAY = 1000L;

    }
}
