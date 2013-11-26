/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import java.util.LinkedList;
import spaceisnear.Context;
import spaceisnear.game.layer.AtmosphericLayer;
import spaceisnear.game.messages.HurtMessage;
import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessageToSend;
import spaceisnear.game.objects.Position;

/**
 *
 * @author White Oak
 */
public class HealthComponent extends Component {

    @Override
    public void processMessage(Message message) {
	AtmosphericLayer atmosphere = getContext().getCameraMan().getAtmosphere();
	LinkedList<Component> components = getOwner().getComponents();
	Position p = null;
	for (Component component : components) {
	    if (component instanceof PositionComponent) {
		p = ((PositionComponent) component).getPosition();
	    }
	}
	if (!atmosphere.isBreatheable(p.getX(), p.getY())) {
	    HurtMessage hurtMessage = new HurtMessage(5, HurtMessage.Type.SUFFOCATING);
	    getContext().sendToID(hurtMessage, getOwnerId());
	    getContext().sendToID(new MessageToSend(hurtMessage), Context.NETWORKING_ID);
	}
    }

}
