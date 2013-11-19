/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import java.util.ArrayList;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessageMoved;
import spaceisnear.game.objects.Position;

public class PositionComponent extends Component {

    public PositionComponent(Position p) {
	this(p.getX(), p.getY());
    }

    public PositionComponent(int x, int y) {
	ArrayList<ComponentState> states = getStates();
	states.add(new ComponentState("x", x));
	states.add(new ComponentState("x", y));
    }

    public int getX() {
	return (Integer) getStates().get(0).getValue();
    }

    public int getY() {
	return (Integer) getStates().get(1).getValue();
    }

    public void setX(int x) {
	getStates().get(0).setValue(x);
    }

    public void setY(int y) {
	getStates().get(1).setValue(y);
    }

    @Override
    public void processMessage(Message message) {
	switch (message.getMessageType()) {
	    case MOVED:
		MessageMoved messagem = (MessageMoved) message;
		setX(getX() + messagem.getX());
		setY(getY() + messagem.getY());
		getContext().getCamera().setNewCameraPositionFor(messagem.getX(), messagem.getY());
		break;
	    case TELEPORTED:
		//Note that MessageTeleported is the subclass of MessageMoved
		MessageMoved messagetMessageMoved = (MessageMoved) message;
		setX(messagetMessageMoved.getX());
		setY(messagetMessageMoved.getY());
		break;
	}
    }
}
