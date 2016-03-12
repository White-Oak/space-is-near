/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components.server;

import java.util.List;
import spaceisnear.abstracts.Context;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentType;
import spaceisnear.game.layer.AtmosphericLayer;
import spaceisnear.game.messages.MessageHurt;
import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessageToSend;
import spaceisnear.game.ui.Position;

//
public class BreathingComponent extends Component {

    public BreathingComponent() {
	super(ComponentType.BREATHING);
    }

    @Override
    public void processMessage(Message message) {
//	AtmosphericLayer atmosphere = getContext().getCameraMan().getAtmosphere();
//	List<Component> components = getOwner().getComponents();
//	Position p = null;
//	for (Component component : components) {
//	    if (component instanceof PositionComponent) {
//		p = ((PositionComponent) component).getPosition();
//	    }
//	}
//	if (!atmosphere.isBreatheable(p.getX(), p.getY())) {
//	    HurtMessage hurtMessage = new HurtMessage(5, HurtMessage.Type.SUFFOCATING, getOwnerId());
//	    getContext().sendToID(hurtMessage, getOwnerId());
//	    getContext().sendToID(new MessageToSend(hurtMessage), Context.NETWORKING_ID);
//	}
    }

}
