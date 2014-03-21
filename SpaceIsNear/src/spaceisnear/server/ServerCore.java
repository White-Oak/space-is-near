package spaceisnear.server;

import java.io.IOException;
import java.util.*;
import lombok.Getter;
import spaceisnear.abstracts.AbstractGameObject;
import spaceisnear.abstracts.Context;
import spaceisnear.game.GameContext;
import spaceisnear.game.components.server.HealthComponent;
import spaceisnear.game.layer.AtmosphericLayer;
import spaceisnear.game.layer.ObstaclesLayer;
import spaceisnear.game.messages.*;
import spaceisnear.game.objects.Position;
import spaceisnear.game.objects.items.ItemsReader;
import spaceisnear.server.contexteditors.AtmosphereEditor;
import spaceisnear.server.objects.Player;
import spaceisnear.server.objects.items.*;
import spaceisnear.server.objects.items.scripts.ItemScriptReader;

/**
 * @author white_oak
 */
public class ServerCore implements Runnable {

    @Getter private final ServerContext context;
    private final boolean unbreakable = true;
    @Getter private boolean paused = false;
    private static final int QUANT_TIME = 20;
    private long timePassed;
    private AtmosphereThread at = new AtmosphereThread();

    public ServerCore() throws IOException {
	int width = GameContext.MAP_WIDTH;
	int height = width;
	final ArrayList<AbstractGameObject> objects = new ArrayList<>();
	try {
	    ServerItemsArchive.ITEMS_ARCHIVE = new ServerItemsArchive(ItemsReader.read(), ItemScriptReader.read());
	} catch (Exception ex) {
	    Context.LOG.log(ex);
	}
	ObstaclesLayer obstacles = new ObstaclesLayer(width, height);
	AtmosphericLayer atmosphere = new AtmosphericLayer(width, height);
	context = new ServerContext(new ServerNetworking(this), objects, obstacles, atmosphere);
	//items adding
	ItemAdder itemAdder = new ItemAdder(context);
	itemAdder.addItems();
	Context.LOG.log("done");
	AtmosphereEditor atmosphereEditor = new AtmosphereEditor();
	atmosphereEditor.update(context);
	atmosphereEditor.show();
    }

    @Override
    public void run() {
//	at.start();
	while (unbreakable) {
	    //networking
	    context.getNetworking().processReceivedQueue();
	    //game
	    if (!paused) {
		MessageTimePassed messageTimePassed = new MessageTimePassed(QUANT_TIME);
		context.sendTimePassed(messageTimePassed);
		for (AbstractGameObject gameObject : getContext().getObjects()) {
		    if (gameObject != null) {
			gameObject.process();
		    }
		}
	    }
	    try {
		Thread.sleep(QUANT_TIME);
	    } catch (InterruptedException ex) {
		Context.LOG.log(ex);
	    }
	    sendPressure();
	}
    }

    private void sendPressure() {
	timePassed += QUANT_TIME;
	if (timePassed > 4000 && !context.getPlayers().isEmpty()) {
	    Player get = context.getPlayers().get(0);
	    final int pressure = context.getAtmosphere().getPressure(get.getPosition().getX(), get.getPosition().getY());
//	    context.getNetworking().sendToAll(new MessageLog(new LogString("Pressure: " + pressure, LogLevel.DEBUG)));
	    timePassed = 0;
	}
    }

    private void checkHealthStatuses() {
	for (Player player : getContext().getPlayers()) {
	    Position position = player.getPosition();
	    int pressure = context.getAtmosphere().getPressure(position.getX(), position.getY());
	    if (pressure < AtmosphericLayer.PRESSURE_HARD_TO_BREATH) {
		HurtMessage hurtMessage;
		if (pressure < AtmosphericLayer.PRESSURE_ENOUGH_TO_BREATH) {
		    hurtMessage = new HurtMessage(HealthComponent.SUFFOCATING_DAMAGE, HurtMessage.Type.SUFFOCATING,
			    player.getId());
		} else {
		    hurtMessage = new HurtMessage(HealthComponent.LIGHT_SUFFOCATING_DAMAGE, HurtMessage.Type.SUFFOCATING,
			    player.getId());
		}
		getContext().sendDirectedMessage(hurtMessage);
	    }
	}
    }

    public void pause() {
	paused = true;
    }

    public void unpause() {
	paused = false;
    }

    public void host() throws IOException {
	getContext().getNetworking().host();
    }

    public Player addPlayer() {
	return context.addPlayer();
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
		    Context.LOG.log(ex);
		}
	    }
	}
	public static final long ATMOSPHERE_DELAY = 2000L;

    }
}
