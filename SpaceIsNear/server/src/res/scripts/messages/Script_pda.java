package res.scripts.messages;

import spaceisnear.game.messages.MessageChat;
import spaceisnear.game.messages.MessageType;
import spaceisnear.game.ui.console.ChatString;
import spaceisnear.server.scriptsv2.MessageReceivedScript;

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
		ChatString value = (ChatString) getPropertyMessageValue();
		//Send to player the message
		context.sendToNetwork(new MessageChat(value));
	    }
	}
    }

}
