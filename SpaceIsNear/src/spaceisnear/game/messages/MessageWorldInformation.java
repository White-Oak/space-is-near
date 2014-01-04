/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import spaceisnear.game.bundles.MessageBundle;

public class MessageWorldInformation extends Message implements NetworkableMessage {

    public final int amountOfItems;

    public MessageWorldInformation(int amountOfItems) {
	super(MessageType.WORLD_INFO);
	this.amountOfItems = amountOfItems;
    }

    @Override
    public MessageBundle getBundle() {
	MessageBundle messageBundle = new MessageBundle(getMessageType());
	try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(4); DataOutputStream dataOutputStream = new DataOutputStream(
		byteArrayOutputStream)) {
	    dataOutputStream.writeInt(amountOfItems);
	    messageBundle.bytes = byteArrayOutputStream.toByteArray();
	} catch (IOException ex) {
	    Logger.getLogger(MessageWorldInformation.class.getName()).log(Level.SEVERE, null, ex);
	}
	return messageBundle;
    }

    public static MessageWorldInformation getInstance(byte[] b) {
	try (ByteArrayInputStream bais = new ByteArrayInputStream(b); DataInputStream dis = new DataInputStream(bais)) {
	    int amount = dis.readInt();
	    return new MessageWorldInformation(amount);
	} catch (IOException ex) {
	    Logger.getLogger(MessageWorldInformation.class.getName()).log(Level.SEVERE, null, ex);
	}
	return null;
    }
}
