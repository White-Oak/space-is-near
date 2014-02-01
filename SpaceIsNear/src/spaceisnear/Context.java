/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear;

import java.util.LinkedList;
import java.util.List;
import spaceisnear.game.messages.DirectedMessage;
import spaceisnear.game.messages.Message;
import spaceisnear.game.objects.GameObjectType;

/**
 *
 * @author White Oak
 */
public abstract class Context {

    public final static int NETWORKING_ID = 0;

    public abstract void sendThemAll(Message m);

    public abstract void sendToID(Message m, int id);

    public synchronized void sendDirectedMessage(DirectedMessage directedMessage) {
	sendToID(directedMessage, directedMessage.getId());
    }

    public abstract List<AbstractGameObject> getObjects();

    public List<AbstractGameObject> itemsOn(int x, int y) {
	LinkedList<AbstractGameObject> items = new LinkedList<>();
	for (AbstractGameObject abstractGameObject : getObjects()) {
	    if (abstractGameObject.getType() == GameObjectType.ITEM && abstractGameObject.getPosition().equals(x, y)) {
		items.add(abstractGameObject);
	    }
	}
	return items;
    }
}
