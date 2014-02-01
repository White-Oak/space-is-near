/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages.service.onceused;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import spaceisnear.game.bundles.MessageBundle;
import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessageType;
import spaceisnear.game.messages.NetworkableMessage;

public class MessageWorldInformation extends Message implements NetworkableMessage {

    public final int amountOfItems;
    public final int propertyAmount;

    public MessageWorldInformation(int amountOfItems, int propertyAmount) {
	super(MessageType.WORLD_INFO);
	this.amountOfItems = amountOfItems;
	this.propertyAmount = propertyAmount;
    }

    @Override
    public MessageBundle getBundle() {
	MessageBundle messageBundle = new MessageBundle(getMessageType());
	try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(4); DataOutputStream dataOutputStream = new DataOutputStream(
		byteArrayOutputStream)) {
	    dataOutputStream.writeInt(amountOfItems);
	    dataOutputStream.writeInt(propertyAmount);
	    messageBundle.bytes = byteArrayOutputStream.toByteArray();
	} catch (IOException ex) {
	    Logger.getLogger(MessageWorldInformation.class.getName()).log(Level.SEVERE, null, ex);
	}
	return messageBundle;
    }

    public static MessageWorldInformation getInstance(byte[] b) {
	try (ByteArrayInputStream bais = new ByteArrayInputStream(b); DataInputStream dis = new DataInputStream(bais)) {
	    int amount = dis.readInt();
	    int prop = dis.readInt();
	    return new MessageWorldInformation(amount, prop);
	} catch (IOException ex) {
	    Logger.getLogger(MessageWorldInformation.class.getName()).log(Level.SEVERE, null, ex);
	}
	return null;
    }
}
