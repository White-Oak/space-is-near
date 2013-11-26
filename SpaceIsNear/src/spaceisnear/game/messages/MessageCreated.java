/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import lombok.Getter;
import spaceisnear.game.bundles.MessageBundle;

public class MessageCreated extends Message implements NetworkableMessage {

    @Getter private final String json;

    public MessageCreated(String json) {
	super(MessageType.CREATED);
	this.json = json;
    }

    @Override
    public MessageBundle getBundle() {
	MessageBundle messageBundle = new MessageBundle(getMessageType());
	messageBundle.bytes = json.getBytes();
	return messageBundle;
    }

    public static MessageCreated getInstance(byte[] b) {
	return new MessageCreated(new String(b));
    }
}
