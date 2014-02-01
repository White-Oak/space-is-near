// Generated by delombok at Sun Dec 01 13:28:22 MSK 2013
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.server;

import java.io.IOException;
import spaceisnear.server.objects.Player;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.SlickException;
import spaceisnear.AbstractGameObject;
import spaceisnear.game.GameContext;
import spaceisnear.game.components.server.HealthComponent;
import spaceisnear.game.ui.console.LogLevel;
import spaceisnear.game.ui.console.LogString;
import spaceisnear.game.layer.AtmosphericLayer;
import spaceisnear.game.layer.ObstaclesLayer;
import spaceisnear.game.messages.MessageDied;
import spaceisnear.game.messages.MessageKnockbacked;
import spaceisnear.game.messages.MessageLog;
import spaceisnear.game.messages.MessagePaused;
import spaceisnear.game.messages.MessageToSend;
import spaceisnear.game.messages.MessageUnpaused;
import spaceisnear.game.objects.Position;
import spaceisnear.game.objects.items.ItemsReader;
import spaceisnear.server.objects.items.ItemAdder;
import spaceisnear.server.objects.items.ServerItemsArchive;

/**
 * @author white_oak
 */
public class ServerCore implements Runnable {

    private final ServerContext context;
    private final boolean unbreakable = true;
    private boolean paused = false;
    private static final long QUANT_TIME = 20;
    private boolean alreadyPaused;
    private long timePassed;
    private AtmosphereThread at = new AtmosphereThread();

    public ServerCore() throws IOException {
	int width = GameContext.MAP_WIDTH;
	int height = width;
	final ArrayList<AbstractGameObject> objects = new ArrayList<>();
	try {
	    ServerItemsArchive.itemsArchive = new ServerItemsArchive(ItemsReader.read());
	} catch (SlickException ex) {
	    Logger.getLogger(ServerCore.class.getName()).log(Level.SEVERE, null, ex);
	} catch (Exception ex) {
	    Logger.getLogger(ServerCore.class.getName()).log(Level.SEVERE, null, ex);
	}
	ObstaclesLayer obstacles = new ObstaclesLayer(width, height);
	AtmosphericLayer atmosphere = new AtmosphericLayer(width, height);
	context = new ServerContext(new ServerNetworking(this), objects, obstacles, atmosphere);
	//items adding
	ItemAdder itemAdder = new ItemAdder(context);
	itemAdder.addItems();
    }

    @Override
    public void run() {
	at.start();
	while (unbreakable) {
	    //networking
	    context.getNetworking().processReceivedQueue();
	    //game
	    if (!paused) {
		for (AbstractGameObject gameObject : getContext().getObjects()) {
		    if (gameObject != null) {
			gameObject.process();
		    }
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
	    sendPressure();
	}
    }

    private void sendPressure() {
	timePassed += QUANT_TIME;
	if (timePassed > 4000 && !context.getPlayers().isEmpty()) {
	    Player get = context.getPlayers().get(0);
	    final int pressure = context.getAtmosphere().getPressure(get.getPosition().getX(), get.getPosition().getY());
	    context.getNetworking().sendToAll(new MessageLog(new LogString("Pressure: " + pressure, LogLevel.DEBUG)));
	    timePassed = 0;
	}
    }

    private void checkHealthStatuses() {
	for (Player player : getContext().getPlayers()) {
	    HealthComponent hc = player.getHealthComponent();
	    Position position = player.getPosition();
	    int pressure = context.getAtmosphere().getPressure(position.getX(), position.getY());
	    if (pressure < AtmosphericLayer.PRESSURE_HARD_TO_BREATH) {
		context.getNetworking().log(new LogString(player.getNickname() + " задыхается.", LogLevel.TALKING, position));
		if (pressure < AtmosphericLayer.PRESSURE_ENOUGH_TO_BREATH) {
		    hc.changeHealth(HealthComponent.SUFFOCATING_DAMAGE);
		}
	    }
	    switch (hc.getState()) {
		case CRITICICAL:
		    MessageKnockbacked messageKnockbacked = new MessageKnockbacked(player.getId());
		    getContext().sendToID(messageKnockbacked, player.getId());
		    getContext().sendToID(new MessageToSend(messageKnockbacked), player.getId());
		    context.getNetworking().log(new LogString(player.getNickname() + " упал оземь.", LogLevel.TALKING, position));
		    break;

		case DEAD:
		    MessageDied messageDied = new MessageDied(player.getId());
		    getContext().sendToID(messageDied, player.getId());
		    getContext().sendToID(new MessageToSend(messageDied), player.getId());
		    context.getNetworking().log(new LogString(player.getNickname() + " перестал дышать.", LogLevel.TALKING, position));
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

    private class AtmosphereThread extends Thread {

	@Override
	public void run() {
	    while (unbreakable) {
		if (!paused) {
		    context.getAtmosphere().tickAtmosphere(context);
		    checkHealthStatuses();
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
