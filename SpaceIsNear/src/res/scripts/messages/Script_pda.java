package res.scripts.messages;

import spaceisnear.server.scriptsv2.MessageReceivedScript;
import spaceisnear.game.messages.MessageType;

/**
 *
 * @author White Oak
 */
public class Script_pda extends MessageReceivedScript {

    @Override
    public void script() {
	MessageType type = getMessageType();
	if (type == MessageType.PROPERTY_SET) {
	    String name = getPropertyMessageName();
	    if ("messages".equals(name)) {
		//Tell method that we are fine with handling the message by ourself
		dontProcessOnYourOwn();
		//Add to property
		String value = (String) getPropertyMessageValue();
		value += System.lineSeparator() + value;
		//Send to player the message
		sendPlayerPrivateMessage(value);
	    }
	}
    }

}
