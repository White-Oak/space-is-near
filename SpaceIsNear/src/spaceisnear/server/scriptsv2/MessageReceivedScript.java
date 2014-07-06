package spaceisnear.server.scriptsv2;

import spaceisnear.game.components.server.VariablePropertiesComponent;
import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessageType;
import spaceisnear.game.messages.properties.MessagePropertySet;
import spaceisnear.server.ServerContext;
import spaceisnear.server.objects.items.StaticItem;

public abstract class MessageReceivedScript extends ItemScript {

    protected Message message;
    private VariablePropertiesComponent vpc;

    public void init(ServerContext context, StaticItem item, Message m, VariablePropertiesComponent vpc) {
	super.init(context, item);
	message = m;
	this.vpc = vpc;
    }

    protected MessageType getMessageType() {
	return message.getMessageType();
    }

    protected void dontProcessOnYourOwn() {
	vpc.setDontProcess(true);
    }

    protected String getPropertyMessageName() {
	assert message.getMessageType() == MessageType.PROPERTY_SET;
	return ((MessagePropertySet) message).getName();
    }

    protected void sendPlayerPrivateMessage(String value) {
    }

    protected Object getPropertyMessageValue() {
	assert message.getMessageType() == MessageType.PROPERTY_SET;
	return ((MessagePropertySet) message).getValue();
    }
}
