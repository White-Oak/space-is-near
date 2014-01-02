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

public class MessageYourPlayerDiscovered extends Message implements NetworkableMessage {

    private final int playerID;

    public int getPlayerID() {
	return playerID;
    }

    public MessageYourPlayerDiscovered(int playerID) {
	super(MessageType.DISCOVERED_PLAYER);
	this.playerID = playerID;
    }

    @Override
    public MessageBundle getBundle() {
	try {
	    MessageBundle messageBundle = new MessageBundle(getMessageType());
	    try (ByteArrayOutputStream baos = new ByteArrayOutputStream(4); DataOutputStream dos = new DataOutputStream(baos)) {
		dos.writeInt(playerID);
		messageBundle.bytes = baos.toByteArray();
	    }
	    return messageBundle;
	} catch (IOException ex) {
	    Logger.getLogger(MessageYourPlayerDiscovered.class.getName()).log(Level.SEVERE, null, ex);
	}
	return null;
    }

    public static MessageYourPlayerDiscovered getInstance(byte[] b) {
	int playerID = -1;
	try (ByteArrayInputStream bais = new ByteArrayInputStream(b); DataInputStream dis = new DataInputStream(bais)) {
	    playerID = dis.readInt();
	} catch (IOException ex) {
	    Logger.getLogger(MessageYourPlayerDiscovered.class.getName()).log(Level.SEVERE, null, ex);
	}
	return new MessageYourPlayerDiscovered(playerID);
    }

}
