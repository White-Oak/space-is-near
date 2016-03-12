package spaceisnear.game;

import java.util.*;
import lombok.Getter;
import me.whiteoak.minlog.Log;
import spaceisnear.abstracts.AbstractGameObject;
import spaceisnear.abstracts.Context;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.PositionComponent;
import spaceisnear.game.components.client.PaintableComponent;
import spaceisnear.game.components.client.PatientComponent;
import spaceisnear.game.components.server.context.ServerContextMenu;
import spaceisnear.game.messages.*;
import spaceisnear.game.objects.*;

/**
 * @author LPzhelud
 */
public final class GameContext extends Context {

    @Getter private final List<PaintableComponent> paintables = Collections.synchronizedList(new SortedPaintablesList());
    public static final int TILE_HEIGHT = 32;
    public static final int TILE_WIDTH = 32;
    public static final int MAP_WIDTH = 64;
    public static final int MAP_HEIGHT = 64;
    public static float SCALING_X = 1f, SCALING_Y = 1f;
    @Getter private final List<AbstractGameObject> animateds = new ArrayList<>();
    @Getter private int playerID = -1;
    public static final int HIDDEN_CLIENT_OBJECTS = 1;
    private final static MessageAnimationStep MESSAGE_ANIMATION_STEP = new MessageAnimationStep();
    //
    public static final int PATIENCE_ID = -3;
    //
    @Getter private final Engine engine;

    public boolean exists(int objectId) {
	return engine.getObjects().containsKey(objectId);
    }

    public GameContext(Engine engine) {
	super(engine.getObjects());
	this.engine = engine;
	addObject(new NetworkingObject(engine.getNetworking()), Context.NETWORKING_ID);
	addObject(new ClientGameObject(GameObjectType.PATIENCE) {
	    {
		addComponents(new PatientComponent());
	    }
	},
		GameContext.PATIENCE_ID);
    }

    @Override
    public void sendThemAll(Message m) {
	if (!(m instanceof DirectedMessage)) {
	    objects.values().forEach(gameObject -> gameObject.message(m));
	} else {
	    System.out.println(
		    "[WARNING]: There was a directed message of type " + m.getMessageType() + " that was tried to be sent to everyone");
	}
    }

    @Override
    public void sendToID(Message m, int id) {
	objects.get(id).message(m);
    }

    private void addPaintable(PaintableComponent pc) {
	paintables.add(pc);
    }

    public synchronized void addObject(ClientGameObject gameObject, int id) {
	assert !objects.containsKey(id) : "";
	gameObject.setContext(this);
	gameObject.setId(id);
	gameObject.getComponents().stream()
		.filter(component -> component.isPaintable())
		.map(PaintableComponent.class::cast)
		.forEach(component -> addPaintable(component));
	objects.put(id, gameObject);
    }

    public void removeObject(int id) {
	final AbstractGameObject removed = objects.get(id);
	if (removed != null) {
	    removed.setDestroyed(true);
	    List<Component> components = removed.getComponents();
	    components.stream()
		    .filter(component -> component.isPaintable())
		    .map(PaintableComponent.class::cast)
		    .forEach(pc -> pc.setOwnerDestroyed(true));
	    Log.debug("client", "The old object was removed from the map");
	    objects.remove(id);
	}
    }

    public GamerPlayer getPlayer() {
	final AbstractGameObject get = objects.get(playerID);
	if (get != null) {
	    if (get.getType() == GameObjectType.GAMER_PLAYER) {
		return (GamerPlayer) get;
	    }
	}
	return null;
    }

    public void setCameraToPlayer() {
	GamerPlayer get = (GamerPlayer) objects.get(playerID);
	PositionComponent positionComponent = get.getPositionComponent();
	engine.getCore().getCameraMan().moveCameraToPlayer(positionComponent.getX(), positionComponent.getY());
    }

    public void setNewGamerPlayer(int playerID) {
	Player get = (Player) objects.get(playerID);
	final GamerPlayer gamerPlayer = new GamerPlayer(get);
	objects.put(playerID, gamerPlayer);
	this.playerID = playerID;
    }

    public boolean isLogined() {
	return engine.getNetworking().isLogined();
    }

    public boolean isPlayable() {
	return engine.getNetworking().isPlayable();
    }

    public boolean isJoined() {
	return engine.getNetworking().isJoined();
    }

    public void addAnimated(AbstractGameObject animated) {
	animateds.add(animated);
    }

    public void removeAnimated(AbstractGameObject animated) {
	animateds.remove(animated);
    }

    public void menuWantsToHide() {
	engine.getCore().menuWantsToHide();
    }

    public void updateCurrentMenu(ServerContextMenu update) {
	engine.getCore().updateCurrentMenu(update);
    }

}
