package spaceisnear.game.components.server;

import spaceisnear.abstracts.Context;
import spaceisnear.game.components.*;
import spaceisnear.game.components.server.scriptprocessors.*;
import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.properties.*;
import spaceisnear.game.objects.GameObjectType;
import spaceisnear.game.objects.Position;
import spaceisnear.server.ServerContext;
import spaceisnear.server.objects.items.StaticItem;

/**
 *
 * @author White Oak
 */
public class VariablePropertiesComponent extends Component {

    private MessagePropertySetProcessingScriptProccessor mpsp;

    public VariablePropertiesComponent() {
	super(ComponentType.VARIABLES);
    }

    @Override
    public void processMessage(Message message) {
	switch (message.getMessageType()) {
	    case PROPERTY_SET:
		MessagePropertySet mps = (MessagePropertySet) message;
		setDontProcess(false);
		if (getOwner().getType() == GameObjectType.ITEM) {
		    try {
			//Context.LOG.log("Trying to script processing message");
			ServerContext context = (ServerContext) getContext();
			mpsp = new MessagePropertySetProcessingScriptProccessor(context, this, mps);
			mpsp.run();
		    } catch (ClassCastException e) {
			Context.LOG.log(e);
		    }
		}
		if (!isDontProcess()) {
		    processPropertySetMessage(mps);
		}
		break;
	    case INTERACTED:
		if (getOwner().getType() == GameObjectType.ITEM) {
		    try {
			//Context.LOG.log("Trying to script interaction");
			ServerContext context = (ServerContext) getContext();
			InteractionScriptProccessor interactionScriptProccessor = new InteractionScriptProccessor(context, this, null);
			interactionScriptProccessor.run();
		    } catch (ClassCastException e) {
			e.printStackTrace();
		    }
		}
	}
    }

    private void processPropertySetMessage(MessagePropertySet mps) {
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

    public void setProperty(String name, Object value) {
	addState(name, value);
    }

    public Object getProperty(String name) {
	return getStateValueNamed(name);
    }
}
