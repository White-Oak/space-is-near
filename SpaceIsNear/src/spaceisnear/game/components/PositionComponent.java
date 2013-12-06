/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import spaceisnear.Context;
import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessageMoved;
import spaceisnear.game.objects.Position;

public class PositionComponent extends Component {

    public PositionComponent(Position p) {
	super(ComponentType.POSITION);
	addState(new ComponentState("position", p));
    }

    public PositionComponent(int x, int y) {
	this(new Position(x, y));
    }

    private PositionComponent() {
	super(ComponentType.POSITION);
    }

    @Override
    public Position getPosition() {
	return (Position) getStateValueNamed("position");
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
		int newX = getX() + messagem.getX();
		int newY = getY() + messagem.getY();
		if (((Context) getContext()).getCameraMan().getObstacles().isReacheable(newX, newY)) {
		    setX(newX);
		    setY(newY);
		}
		break;
	    case TELEPORTED:
		//Note that MessageTeleported is the subclass of MessageMoved
		MessageMoved messagetMessageMoved = (MessageMoved) message;
		//here no check for obstacles
		setX(messagetMessageMoved.getX());
		setY(messagetMessageMoved.getY());
		break;
	}
    }
}
