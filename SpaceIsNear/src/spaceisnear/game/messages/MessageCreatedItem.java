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
import spaceisnear.game.objects.Position;

public class MessageCreatedItem extends MessageCreated {

    private final int id;
    private final Position p;

    public MessageCreatedItem(int id, Position p) {
	super(null);
	this.id = id;
	this.p = p;
    }

    @Override
    public MessageBundle getBundle() {
	MessageBundle messageBundle = new MessageBundle(MessageType.CREATED_SIMPLIFIED);
	try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
	    try (DataOutputStream dos = new DataOutputStream(baos)) {
		dos.writeInt(id);
		dos.writeInt(p.getX());
		dos.writeInt(p.getY());
	    }
	    messageBundle.bytes = baos.toByteArray();
	} catch (IOException iOException) {
	}
	return messageBundle;
    }

    public static MessageCreatedItem getInstance(byte[] b) {
	try (ByteArrayInputStream bais = new ByteArrayInputStream(b); DataInputStream dis = new DataInputStream(bais)) {
	    int id = dis.readInt();
	    int x = dis.readInt();
	    int y = dis.readInt();
	    Position p = new Position(x, y);
	    return new MessageCreatedItem(id, p);
	} catch (IOException ex) {
	    Logger.getLogger(MessageCreatedItem.class.getName()).log(Level.SEVERE, null, ex);
	}
	return null;
    }

    public int getId() {
	return id;
    }

    public Position getP() {
	return p;
    }

}
