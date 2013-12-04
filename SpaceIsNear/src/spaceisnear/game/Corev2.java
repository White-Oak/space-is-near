// Generated by delombok at Sun Dec 01 13:28:22 MSK 2013
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game;

import spaceisnear.game.objects.GameObject;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import spaceisnear.Context;
import spaceisnear.game.components.PaintableComponent;
import spaceisnear.game.messages.MessageControlled;
import spaceisnear.game.messages.MessageTimePassed;
import spaceisnear.game.messages.MessageToSend;
import spaceisnear.game.objects.NetworkingObject;

/**
 * @author LPzhelud
 */
public class Corev2 extends BasicGameState {

    private GameContext context;
    private final ArrayList<GameObject> objects = new ArrayList<>();
    private static final int QUANT_TIME = 50;
    private int key;
    public static String IP;
    private boolean notpaused;

    @Override
    public void init(GameContainer container, StateBasedGame sbg) throws SlickException {
	context = new GameContext(new CameraMan(), objects, this);
	context.addObject(new NetworkingObject(context));
	context.getCameraMan().setWindowWidth(800);
	context.getCameraMan().setWindowHeight(600);
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
    }

    public void callToConnect() {
	try {
	    context.getNetworking().connect(IP, 54555);
	} catch (IOException ex) {
	    Logger.getLogger(Corev2.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    public boolean isJustConnected() {
	return context.getNetworking().isJustConnected();
    }

    @Override
    public void update(GameContainer container, StateBasedGame sbg, int delta) throws SlickException {
	if (notpaused) {
	    MessageControlled mc = checkKeys();
	    //1 quant of time is 50L by default
	    int quants = delta / QUANT_TIME;
	    MessageTimePassed messageTimePassed = new MessageTimePassed(quants);
	    context.sendThemAll(messageTimePassed);
	    //
	    int playerID = context.getPlayerID();
	    if (mc != null && playerID != -1) {
		MessageToSend messageToSend = new MessageToSend(mc);
		context.sendToID(messageToSend, Context.NETWORKING_ID);
	    }
	    for (GameObject gameObject : objects) {
		gameObject.process();
	    }
	}
    }

    @Override
    public void keyPressed(int key, char c) {
	this.key = key;
    }

    @Override
    public void keyReleased(int key, char c) {
	this.key = 0;
    }

    private MessageControlled checkKeys() {
	MessageControlled mc = null;
	switch (key) {
	    case Input.KEY_UP:
		mc = new MessageControlled(MessageControlled.Type.UP, context.getPlayerID());
		break;
	    case Input.KEY_DOWN:
		mc = new MessageControlled(MessageControlled.Type.DOWN, context.getPlayerID());
		break;

	    case Input.KEY_LEFT:
		mc = new MessageControlled(MessageControlled.Type.LEFT, context.getPlayerID());
		break;

	    case Input.KEY_RIGHT:
		mc = new MessageControlled(MessageControlled.Type.RIGHT, context.getPlayerID());
		break;
	}
	return mc;
    }

    @Override
    public void render(GameContainer container, StateBasedGame sbg, Graphics g) throws SlickException {
	context.getCameraMan().moveCamera(g);
	context.getCameraMan().paint(g);
	for (PaintableComponent paintableComponent : context.getPaintables()) {
	    paintableComponent.paint(g);
	}
	context.getCameraMan().unmoveCamera(g);
    }

    @Override
    public int getID() {
	return 3;
    }

    public void pause() {
	notpaused = false;
    }

    public void unpause() {
	notpaused = true;
	System.out.println("Client has continued his work");
    }

    public boolean isNotpaused() {
	return this.notpaused;
    }
}
