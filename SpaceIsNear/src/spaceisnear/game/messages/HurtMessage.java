// Generated by delombok at Sun Dec 01 13:28:22 MSK 2013
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import spaceisnear.Utils;
import spaceisnear.game.bundles.MessageBundle;

/**
 * Sent both to client and server if player was hurt. Otherwise \u0432\u0402\u201d sent only to server.
 *
 * @author White Oak
 */
public class HurtMessage extends DirectedMessage implements NetworkableMessage {

    private final int damage;
    private final Type type;

    public HurtMessage(int damage, Type type, int id) {
	super(MessageType.HURT, id);
	this.damage = damage;
	this.type = type;
    }

    public enum Type {

	BLUNT,
	SUFFOCATING,
	BLEEDING,
	RADIOACTIVE,
	TOXIN;

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

    public int getDamage() {
	return this.damage;
    }

    public Type getType() {
	return this.type;
    }
}
