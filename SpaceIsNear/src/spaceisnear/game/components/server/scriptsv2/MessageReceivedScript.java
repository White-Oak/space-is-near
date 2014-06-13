package spaceisnear.game.components.server.scriptsv2;

import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessageType;
import spaceisnear.game.messages.properties.MessagePropertySet;

public abstract class MessageReceivedScript extends ItemScript {

    protected Message message;

    protected MessageType getMessageType() {
	return message.getMessageType();
    }

    protected void dontProcessOnYourOwn() {

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
