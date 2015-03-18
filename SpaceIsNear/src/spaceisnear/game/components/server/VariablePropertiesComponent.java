package spaceisnear.game.components.server;

import com.esotericsoftware.minlog.Logs;
import spaceisnear.game.components.*;
import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessageInteracted;
import spaceisnear.game.messages.properties.*;
import spaceisnear.game.objects.GameObjectType;
import spaceisnear.game.objects.Position;
import spaceisnear.server.ServerContext;
import spaceisnear.server.objects.ServerGameObject;
import spaceisnear.server.objects.items.StaticItem;
import spaceisnear.server.scriptsv2.*;

/**
 *
 * @author White Oak
 */
public class VariablePropertiesComponent extends Component {

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
			ScriptsManager scriptsManager = context.getScriptsManager();
			StaticItem owner = (StaticItem) getOwner();
			MessageReceivedScript scriptFor = (MessageReceivedScript) scriptsManager.getScriptFor(
				ScriptsManager.ScriptType.MESSAGES,
				owner.getProperties().getName());
			if (scriptFor != null) {
			    scriptFor.init(context, owner, message, this);
			    scriptFor.script();
			}
		    } catch (ClassCastException e) {
			Logs.error("server", "While trying to get script for changing property", e);
		    }
		}
		if (!isDontProcess()) {
		    processPropertySetMessage(mps);
		}
		break;
	    case INTERACTION:
		if (getOwner().getType() == GameObjectType.ITEM) {
		    try {
			//Context.LOG.log("Trying to script interaction");
			MessageInteracted mu = (MessageInteracted) message;
			ServerContext context = (ServerContext) getContext();
			ScriptsManager scriptsManager = context.getScriptsManager();
			StaticItem owner = (StaticItem) getOwner();
			InteractionScript scriptFor = (InteractionScript) scriptsManager.getScriptFor(
				ScriptsManager.ScriptType.INTERACTION,
				owner.getProperties().getName());
			Logs.debug("server", "Found script interaction");
			if (scriptFor != null) {
			    scriptFor.init(context, owner, (ServerGameObject) context.getObjects().get(mu.getInteractedWith()));
			    scriptFor.script();
			}
		    } catch (ClassCastException e) {
			Logs.error("server", "While trying to get script for interaction", e);
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
