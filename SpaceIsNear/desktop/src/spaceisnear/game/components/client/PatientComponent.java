package spaceisnear.game.components.client;

import spaceisnear.game.GameContext;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentType;
import spaceisnear.game.messages.*;
import spaceisnear.game.messages.properties.MessagePropertable;

/**
 *
 * @author White Oak
 */
public class PatientComponent extends Component {

    public PatientComponent() {
	super(ComponentType.PATIENT_CLIENT);
    }

    @Override
    public void processMessage(Message message) {
	if (message.getMessageType() == MessageType.NOT_READY_YET) {
	    final MessageNotReadyYet moinr = (MessageNotReadyYet) message;
	    final MessagePropertable messagePropertable = moinr.getMessage();
	    final GameContext gameContext = (GameContext) getContext();
	    if (messagePropertable.canBeApplied(gameContext)) {
		messagePropertable.processForClient(gameContext);
	    } else {
//		System.out.println(moinr.getTimes());
//		if (moinr.getTimes() == 100) {
//		    Logs.error("client", "Well it is enough.");
//		}
		gameContext.sendDirectedMessage(moinr.onceAgain());
	    }
	}
    }

}
