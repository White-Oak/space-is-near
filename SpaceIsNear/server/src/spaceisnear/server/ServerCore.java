package spaceisnear.server;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import me.whiteoak.minlog.Log;
import spaceisnear.abstracts.AbstractGameObject;
import spaceisnear.abstracts.Context;
import spaceisnear.game.components.server.HealthComponent;
import spaceisnear.game.layer.AtmosphericLayer;
import spaceisnear.game.layer.ObstaclesLayer;
import spaceisnear.game.messages.*;
import spaceisnear.game.objects.items.ItemsReader;
import spaceisnear.game.ui.Position;
import spaceisnear.server.chunks.ChunkManager;
import spaceisnear.server.contexteditors.ContextEditorsManager;
import spaceisnear.server.objects.Player;
import spaceisnear.server.objects.items.*;
import spaceisnear.server.scriptsv2.ItemScriptReader;

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
    private ContextEditorsManager editorsManager = new ContextEditorsManager();
    private static final MessageTimePassed MESSAGE_TIME_PASSED = new MessageTimePassed(QUANT_TIME);

    public ServerCore() throws IOException {
	int width = Context.MAP_WIDTH;
	int height = width;
	final HashMap<Integer, AbstractGameObject> objects = new HashMap<>();
	try {
	    ServerItemsArchive.ITEMS_ARCHIVE = new ServerItemsArchive(ItemsReader.read(), ItemScriptReader.read());
	} catch (Exception ex) {
	    Log.error("server", "While initializating ITEMS_ARCHIVE", ex);
	}
	ObstaclesLayer obstacles = new ObstaclesLayer(width, height);
	AtmosphericLayer atmosphere = new AtmosphericLayer(width, height);
	context = new ServerContext(new ServerNetworking(this, new ChunkManager(width, height)),
		objects, obstacles, atmosphere);
	//items adding
	ItemAdder itemAdder = new ItemAdder(context);
	itemAdder.addItems();
//	editorsManager.addDefaultCase();
//	editorsManager.startUpdateCycle(context);
    }

    @Override
    public void run() {
	at.start();
	while (unbreakable) {
	    //networking
	    context.getNetworking().processReceivedQueue();
	    context.getNetworking().checkForNewChunks();
	    //game
	    if (!paused) {
		context.sendTimePassed(MESSAGE_TIME_PASSED);
		getContext().getObjects()
			.values()
			.stream()
			.filter(gameObject -> gameObject != null)
			.forEach(gameObject -> gameObject.process());
	    }
	    try {
		TimeUnit.MILLISECONDS.sleep(QUANT_TIME);
	    } catch (InterruptedException ex) {
		Log.error("server", "While sleeping in server update thread", ex);
	    }
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
	getContext().getPlayers().forEach((player) -> {
	    Position position = player.getPosition();
	    final AtmosphericLayer atmosphere = context.getAtmosphere();
	    if (atmosphere.hardToBreath(position)) {
		MessageHurt hurtMessage;
		if (atmosphere.notEnoughToBreath(position)) {
		    hurtMessage = new MessageHurt(HealthComponent.SUFFOCATING_DAMAGE, MessageHurt.Type.SUFFOCATING,
			    player.getId());
		} else {
		    hurtMessage = new MessageHurt(HealthComponent.LIGHT_SUFFOCATING_DAMAGE, MessageHurt.Type.SUFFOCATING,
			    player.getId());
		}
		getContext().sendDirectedMessage(hurtMessage);
	    }
	});
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
		    Log.error("server", "While sleeping in atmosphere thread", ex);
		}
	    }
	}
	public static final long ATMOSPHERE_DELAY = 1000L;

    }
}
