/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import spaceisnear.game.bundles.Bundle;
import spaceisnear.game.bundles.MessageBundle;

public class MessageClientInformation extends Message implements NetworkableMessage {

    @Getter private final String IP;
    @Getter private final String desiredNickname;

    public MessageClientInformation(String IP, String desiredNickname) {
	super(MessageType.CLIENT_INFO);
	this.IP = IP;
	this.desiredNickname = desiredNickname;
    }

    @Override
    public Bundle getBundle() {
	MessageBundle messageBundle = new MessageBundle();
	messageBundle.bytes = new Gson().toJson(this).getBytes();
	return messageBundle;
    }

    public static MessageClientInformation getInstance(byte[] b) {
	return new Gson().fromJson(new String(b), MessageClientInformation.class);
    }
}
