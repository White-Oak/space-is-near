package spaceisnear.game.messages.service.onceused;

import lombok.Getter;
import spaceisnear.game.bundles.MessageBundle;
import spaceisnear.game.messages.*;

/**
 * Sent only when client is connected to server for the first time.
 *
 * @author White Oak
 */
public class MessagePlayerInformation extends Message implements NetworkableMessage {

    @Getter private final String desiredNickname;

    public MessagePlayerInformation(String desiredNickname) {
	super(MessageType.PLAYER_INFO);
	this.desiredNickname = desiredNickname;
    }

    @Override
    public MessageBundle getBundle() {
	MessageBundle messageBundle = new MessageBundle(getMessageType());
	messageBundle.bytes = desiredNickname.getBytes();
	return messageBundle;
    }

    public static MessagePlayerInformation getInstance(byte[] b) {
	return new MessagePlayerInformation(new String(b));
    }
}
