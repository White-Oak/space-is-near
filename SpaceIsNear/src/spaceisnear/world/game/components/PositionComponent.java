/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.world.game.components;

import lombok.Getter;
import spaceisnear.world.game.GameContext;
import spaceisnear.world.game.GameObject;
import spaceisnear.world.game.messages.Message;
import spaceisnear.world.game.messages.MessageMoved;

public class PositionComponent extends Component {

    @Getter private int x, y;

    public PositionComponent(GameContext context, GameObject owner) {
	super(context, owner);
    }

    @Override
    public void processMessage(Message message) {
	switch (message.getMessageType()) {
	    case MOVED:
		MessageMoved messagem = (MessageMoved) message;
		x += messagem.getX();
		y += messagem.getY();
		break;
	    case TELEPORTED:
		MessageMoved messagetMessageMoved = (MessageMoved) message;
		x = messagetMessageMoved.getX();
		y = messagetMessageMoved.getY();
		break;
	}
    }
}
