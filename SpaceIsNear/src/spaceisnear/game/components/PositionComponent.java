/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessageMoved;
import spaceisnear.game.objects.Position;

public class PositionComponent extends Component {

    PositionComponent() {
    }

    public PositionComponent(Position p) {
	getStates().add(new ComponentState("position", p));
    }

    public PositionComponent(int x, int y) {
	this(new Position(x, y));
    }

    public Position getPosition() {
	return (Position) getStates().get(0).getValue();
    }

    public int getX() {
	return ((Position) getStates().get(0).getValue()).getX();
    }

    public int getY() {
	return ((Position) getStates().get(0).getValue()).getY();
    }

    public void setX(int x) {
	((Position) getStates().get(0).getValue()).setX(x);
    }

    public void setY(int y) {
	((Position) getStates().get(0).getValue()).setY(y);
    }

    @Override
    public void processMessage(Message message) {
	switch (message.getMessageType()) {
	    case MOVED:
		MessageMoved messagem = (MessageMoved) message;
		setX(getX() + messagem.getX());
		setY(getY() + messagem.getY());
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
