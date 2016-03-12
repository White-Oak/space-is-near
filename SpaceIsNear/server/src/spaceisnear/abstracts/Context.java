/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.abstracts;

import java.util.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import spaceisnear.game.messages.*;
import spaceisnear.game.objects.GameObjectType;

/**
 *
 * @author White Oak
 */
@RequiredArgsConstructor public abstract class Context {

    @Getter protected final Map<Integer, AbstractGameObject> objects;
    public final static int NETWORKING_ID = -2;
    public final static int TILE_WIDTH = 32;
    public final static int TILE_HEIGHT = 32;
    public final static int MAP_WIDTH = 128;

    public abstract void sendThemAll(Message m);

    public abstract void sendToID(Message m, int id);

    public final synchronized void sendDirectedMessage(DirectedMessage directedMessage) {
	sendToID(directedMessage, directedMessage.getId());
    }

    public List<AbstractGameObject> itemsOn(int x, int y) {
	LinkedList<AbstractGameObject> items = new LinkedList<>();
	getObjects().values().stream()
		.filter(abstractGameObject
			-> (abstractGameObject.getType() == GameObjectType.ITEM && abstractGameObject.getPosition().equals(x, y)))
		.forEach(abstractGameObject -> items.add(abstractGameObject));
	return items;
    }

    public void sendToNetwork(NetworkableMessage m) {
	sendDirectedMessage(new MessageToSend(m));
    }
}
