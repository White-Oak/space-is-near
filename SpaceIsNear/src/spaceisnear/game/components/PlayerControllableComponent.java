/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import spaceisnear.game.GameContext;
import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessageControlled;
import static spaceisnear.game.messages.MessageControlled.Type.DOWN;
import static spaceisnear.game.messages.MessageControlled.Type.LEFT;
import static spaceisnear.game.messages.MessageControlled.Type.RIGHT;
import static spaceisnear.game.messages.MessageControlled.Type.UP;
import spaceisnear.game.messages.MessageMoved;
import spaceisnear.game.messages.MessageType;
import static spaceisnear.game.messages.MessageType.MOVED;
import static spaceisnear.game.messages.MessageType.TELEPORTED;
import spaceisnear.game.objects.GameObject;

public class PlayerControllableComponent extends Component {

    public PlayerControllableComponent(GameObject owner) {
	getStates().add(new ComponentState("owner", owner));
    }

    private GameObject getOwner() {
	return (GameObject) getStates().get(0).getValue();
    }

    @Override
    public void processMessage(Message message) {
	switch (message.getMessageType()) {
	    case MOVED:
		MessageMoved messagem = (MessageMoved) message;
		((GameContext) getContext()).getCamera().setNewCameraPositionFor(messagem.getX(), messagem.getY());
		break;
	    case TELEPORTED:
		//Note that MessageTeleported is the subclass of MessageMoved
		MessageMoved messagetMessageMoved = (MessageMoved) message;
		((GameContext) getContext()).getCamera().setNewCameraPositionFor(messagetMessageMoved.getX(),
			messagetMessageMoved.getY());
		break;
	    case CONTROLLED:
		MessageControlled mc = (MessageControlled) message;
		MessageMoved mm = null;
		switch (mc.getType()) {
		    case UP:
			mm = new MessageMoved(0, -1, getOwner().getId());
			break;
		    case DOWN:
			mm = new MessageMoved(0, 1, getOwner().getId());
			break;
		    case LEFT:
			mm = new MessageMoved(-1, 0, getOwner().getId());
			break;
		    case RIGHT:
			mm = new MessageMoved(1, 0, getOwner().getId());
			break;
		}
		if (mm != null) {
		    getOwner().message(mm);
		    ((GameContext) getContext()).getNetworking().send(mm);
		}
	}
    }
}
