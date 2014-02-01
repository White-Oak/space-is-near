/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages.propertys;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import spaceisnear.Utils;
import spaceisnear.game.bundles.MessageBundle;
import spaceisnear.game.messages.DirectedMessage;
import spaceisnear.game.messages.MessageType;
import spaceisnear.game.messages.NetworkableMessage;

public class MessagePropertySet extends DirectedMessage implements NetworkableMessage, MessagePropertable {

    @Getter private final String name;
    @Getter private final Object value;
    private final String valueClass;

    public MessagePropertySet(int id, String name, Object value) {
	super(MessageType.PROPERTY_SET, id);
	this.name = name;
	this.value = value;
	valueClass = value.getClass().getName();
    }

    @Override
    public MessageBundle getBundle() {
	MessageBundle messageBundle = new MessageBundle(getMessageType());

	try {
	    try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); DataOutputStream dos = new DataOutputStream(baos)) {
		dos.writeUTF(name);
		dos.writeUTF(Utils.GSON.toJson(value));
		dos.writeUTF(valueClass);
		dos.writeInt(id);
		messageBundle.bytes = baos.toByteArray();
	    }
	} catch (IOException iOException) {
	}

	return messageBundle;
    }

    public static MessagePropertySet getInstance(byte[] b) {
	try {
	    try (ByteArrayInputStream bais = new ByteArrayInputStream(b); DataInputStream dis = new DataInputStream(bais)) {
		String name = dis.readUTF();
		String value = dis.readUTF();
		String valueClass = dis.readUTF();
		Object value1 = Utils.GSON.fromJson(value, Class.forName(valueClass));
		return new MessagePropertySet(dis.readInt(), name, value1);
	    } catch (ClassNotFoundException ex) {
		Logger.getLogger(MessagePropertySet.class.getName()).log(Level.SEVERE, null, ex);
	    }
	} catch (IOException ex) {
	    Logger.getLogger(MessagePropertySet.class.getName()).log(Level.SEVERE, null, ex);
	}
	return null;
    }
}
