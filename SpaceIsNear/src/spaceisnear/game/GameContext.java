// Generated by delombok at Sun Dec 01 13:28:22 MSK 2013
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game;

import box2dLight.RayHandler;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import java.util.*;
import lombok.*;
import spaceisnear.abstracts.*;
import spaceisnear.game.components.*;
import spaceisnear.game.components.client.PaintableComponent;
import spaceisnear.game.components.server.scriptprocessors.context.ServerContextMenu;
import spaceisnear.game.messages.*;
import spaceisnear.game.objects.*;
import spaceisnear.server.ServerContext;

/**
 * @author LPzhelud
 */
@RequiredArgsConstructor public class GameContext extends Context {

    @Getter private final CameraMan cameraMan;
    @Getter private final ArrayList<PaintableComponent> paintables = new ArrayList<>();
    public static final int TILE_HEIGHT = 32;
    public static final int TILE_WIDTH = 32;
    public static final int MAP_WIDTH = 64;
    public static final int MAP_HEIGHT = 64;
    public static float SCALING_X = 1f, SCALING_Y = 1f;
    @Getter private final List<AbstractGameObject> objects;
    private final List<AbstractGameObject> animateds = new ArrayList<>();
    @Getter @Setter private int playerID = -1;
    @Getter private final Corev2 core;
    public static final int HIDDEN_CLIENT_OBJECTS = 1;
    private final static MessageAnimationStep MESSAGE_ANIMATION_STEP = new MessageAnimationStep();
    @Getter private final static World world = new World(new Vector2(), true);
    @Getter private final static RayHandler rayHandler;

    static {
	rayHandler = new RayHandler(world);
	RayHandler.useDiffuseLight(true);
	rayHandler.setCulling(true);
//	rayHandler.setAmbientLight(new Color(1, 1, 1, 0f));
    }

    @Override
    public void sendThemAll(Message m) {
	if (!(m instanceof DirectedMessage)) {
	    objects.forEach(gameObject -> gameObject.message(m));
	}
    }

    public void sendAnimationStep() {
	animateds.forEach(abstractGameObject -> abstractGameObject.message(MESSAGE_ANIMATION_STEP));
    }

    @Override
    public void sendToID(Message m, int id) {
	objects.get(id).message(m);
    }

    public void addPaintable(PaintableComponent pc) {
	paintables.add(pc);
    }

    public synchronized void addObject(ClientGameObject gameObject) {
	gameObject.setContext(this);
	objects.add(gameObject);
	gameObject.setId(objects.size() - 1);
    }

    public void checkSize() {
	while (objects.size() < ServerContext.HIDDEN_SERVER_OBJECTS) {
	    objects.add(null);
	}
    }

    public GamerPlayer getPlayer() {
	return (GamerPlayer) objects.get(playerID);
    }

    public void setCameraToPlayer() {
	GamerPlayer get = (GamerPlayer) objects.get(playerID);
	PositionComponent positionComponent = get.getPositionComponent();
	cameraMan.moveCameraToPlayer(positionComponent.getX(), positionComponent.getY());
    }

    public void setNewGamerPlayer(int playerID) {
	this.playerID = playerID;
	Player get = (Player) objects.get(playerID);
	final GamerPlayer gamerPlayer = new GamerPlayer(get);
	objects.set(playerID, gamerPlayer);
    }

    public boolean isLogined() {
	return core.getNetworking().isLogined();
    }

    public boolean isPlayable() {
	return core.getNetworking().isPlayable();
    }

    public boolean isJoined() {
	return core.getNetworking().isJoined();
    }

    public void addAnimated(AbstractGameObject animated) {
	animateds.add(animated);
    }

    public void removeAnimated(AbstractGameObject animated) {
	animateds.remove(animated);
    }

    public void menuWantsToHide() {
	core.menuWantsToHide();
    }

    public void updateCurrentMenu(ServerContextMenu update) {
	core.updateCurrentMenu(update);
    }

}
