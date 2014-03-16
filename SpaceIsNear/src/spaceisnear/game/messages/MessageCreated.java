// Generated by delombok at Sun Dec 01 13:28:22 MSK 2013
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import lombok.Getter;
import spaceisnear.game.GameContext;
import spaceisnear.game.objects.*;
import spaceisnear.starting.LoadingScreen;

/**
 * Sent only by server as a result of Items' interactions.
 *
 * @author White Oak
 */
public class MessageCreated extends Message implements NetworkableMessage {

    @Getter private final GameObjectType type;

    public MessageCreated(GameObjectType type) {
	super(MessageType.CREATED_SIMPLIFIED);
	this.type = type;
    }

    protected MessageCreated(GameObjectType type, MessageType type1) {
	super(type1);
	this.type = type;
    }

    @Override
    public void processForClient(GameContext context) {
	ClientGameObject gameObject = null;
	switch (getType()) {
	    case PLAYER:
		gameObject = new Player();
		break;
	}
	if (gameObject != null) {
	    context.addObject(gameObject);
	    LoadingScreen.CURRENT_AMOUNT++;
	}
    }

}
