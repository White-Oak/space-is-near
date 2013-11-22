/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import com.google.gson.Gson;
import lombok.Getter;
import spaceisnear.game.bundles.Bundle;
import spaceisnear.game.bundles.MessageBundle;

public class MessageWorldSent extends Message implements NetworkableMessage {

    @Getter private final String world;

    public MessageWorldSent(String world) {
	super(MessageType.WORLD_SENT);
	this.world = world;
    }

    @Override
    public MessageBundle getBundle() {
	MessageBundle messageBundle = new MessageBundle(getMessageType());
	messageBundle.bytes = new Gson().toJson(this).getBytes();
	return messageBundle;
    }

    public static MessageWorldSent getInstance(byte[] b) {
	return new Gson().fromJson(new String(b), MessageWorldSent.class);
    }
}
