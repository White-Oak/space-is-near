package spaceisnear.game.components.server;

import lombok.Setter;
import spaceisnear.game.components.*;
import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.properties.*;
import spaceisnear.game.objects.Position;
import spaceisnear.server.ServerContext;
import spaceisnear.server.objects.items.StaticItem;

/**
 *
 * @author White Oak
 */
public class VariablePropertiesComponent extends Component {

    @Setter private boolean dontProcess = false;
    private MessageProcessingScriptProccessor mpsp;

    public VariablePropertiesComponent() {
	super(ComponentType.VARIABLES);
    }

    @Override
    public void processMessage(Message message) {
	switch (message.getMessageType()) {
	    case PROPERTY_SET:
		MessagePropertySet mps = (MessagePropertySet) message;
		dontProcess = false;
		try {
		    System.out.println("Trying to script");
		    ServerContext context = (ServerContext) getContext();
		    System.out.println("Still trying to script");
		    if (mpsp == null) {
			System.out.println("No mpsp -- will create it");
			mpsp = new MessageProcessingScriptProccessor(context, this, mps);
		    }
		    mpsp.run();
		} catch (ClassCastException e) {
		    e.printStackTrace();
		}
		if (!dontProcess) {
		    switch (mps.getName()) {
			case "pull":
			    if (((Integer) mps.getValue()) != -1) {
				Integer value = (Integer) mps.getValue();
				StaticItem get = (StaticItem) getContext().getObjects().get(value);
				//checked if unstucked
				Object property = get.getVariableProperties().getProperty("stucked");
				if (property != null && !(Boolean) property) {
				    //1-tile-range of pulling
				    Position positionToPull = get.getPosition();
				    Position position = getPosition();
				    if (Math.abs(positionToPull.getX() - position.getX()) <= 1 && Math.abs(
					    positionToPull.getY() - position.getY()) <= 1) {
					setProperty(mps.getName(), mps.getValue());
				    }
				}
			    } else {
				setProperty(mps.getName(), mps.getValue());
			    }
			    break;
		    }
		}
		break;
	}
    }

    public void setProperty(String name, Object value) {
	addState(name, value);
    }

    public Object getProperty(String name) {
	return getStateValueNamed(name);
    }
}
