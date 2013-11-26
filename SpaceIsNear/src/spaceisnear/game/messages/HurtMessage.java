/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import lombok.Getter;
import spaceisnear.Utils;
import spaceisnear.game.bundles.Bundle;
import spaceisnear.game.bundles.MessageBundle;

public class HurtMessage extends DirectedMessage implements NetworkableMessage {

    @Getter private final int damage;
    @Getter private final Type type;

    public HurtMessage(int damage, Type type) {
	super(MessageType.HURT);
	this.damage = damage;
	this.type = type;
    }

    public enum Type {

	BLUNT, SUFFOCATING, BLEEDING, RADIOACTIVE, TOXIN
    }

    @Override
    public MessageBundle getBundle() {
	MessageBundle messageBundle = new MessageBundle(getMessageType());
	messageBundle.bytes = Utils.GSON.toJson(this).getBytes();
	return messageBundle;
    }

    public static HurtMessage getInstance(byte[] b) {
	return Utils.GSON.fromJson(new String(b), HurtMessage.class);
    }
}
