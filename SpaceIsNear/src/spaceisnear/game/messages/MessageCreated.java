// Generated by delombok at Sun Dec 01 13:28:22 MSK 2013
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import lombok.Getter;
import spaceisnear.Utils;
import spaceisnear.game.bundles.MessageBundle;
import spaceisnear.game.objects.GameObjectType;

/**
 * Sent only by server as a result of Items' interactions.
 *
 * @author White Oak
 */
public class MessageCreated extends Message implements NetworkableMessage {
//
//    private final String json;
//
//    public MessageCreated(String json) {
//	super(MessageType.CREATED);
//	this.json = json;
//    }
//
//    @Override
//    public MessageBundle getBundle() {
//	MessageBundle messageBundle = new MessageBundle(getMessageType());
//	messageBundle.bytes = json.getBytes();
//	return messageBundle;
//    }
//
//    public static MessageCreated getInstance(byte[] b) {
//	return new MessageCreated(new String(b));
//    }
//
//    public String getJson() {
//	return this.json;
//    }

    @Getter private final GameObjectType type;

    public MessageCreated(GameObjectType type) {
	super(MessageType.CREATED_SIMPLIFIED);
	this.type = type;
    }

    protected MessageCreated(GameObjectType type, MessageType type1) {
	super(type1);
	this.type = type;
    }

    @Override
    public MessageBundle getBundle() {
	return new MessageBundle(Utils.GSON.toJson(type).getBytes(), getMessageType());
    }

    public static MessageCreated getInstance(byte[] b) {
	return new MessageCreated(Utils.GSON.fromJson(new String(b), GameObjectType.class));
    }
}
