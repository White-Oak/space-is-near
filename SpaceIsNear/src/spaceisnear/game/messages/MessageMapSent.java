/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import com.google.gson.Gson;
import lombok.Getter;
import spaceisnear.game.bundles.Bundle;
import spaceisnear.game.bundles.MessageBundle;

public class MessageMapSent extends Message implements NetworkableMessage {

    @Getter private final String map;

    public MessageMapSent(String map) {
	super(MessageType.MAP_SENT);
	this.map = map;
    }

    @Override
    public Bundle getBundle() {
	MessageBundle messageBundle = new MessageBundle(getMessageType().ordinal());
	messageBundle.bytes = new Gson().toJson(this).getBytes();
	return messageBundle;
    }

    public static MessageMapSent getInstance(byte[] b) {
	return new Gson().fromJson(new String(b), MessageMapSent.class);
    }
}
