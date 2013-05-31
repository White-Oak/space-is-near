/*
 * To change this template, choose Tools | Templates
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
import lombok.*;
import spaceisnear.game.bundles.Bundle;
import spaceisnear.game.bundles.MessageBundle;

public class MessageMoved extends Message implements NetworkableMessage {

    @Getter private final int x, y;
    @Getter private final int id;

    public MessageMoved(int x, int y, int id) {
	super(MessageType.MOVED);
	this.x = x;
	this.y = y;
	this.id = id;
    }

    /**
     * Only for MessageTeleported subclass
     *
     */
    MessageMoved(int x, int y, int id, int unused) {
	super(MessageType.TELEPORTED);
	this.x = x;
	this.y = y;
	this.id = id;
    }

    @Override
    public Bundle getBundle() {
	ByteArrayOutputStream b = null;
	try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); DataOutputStream daos = new DataOutputStream(baos)) {
	    daos.writeInt(x);
	    daos.writeInt(y);
	    daos.writeInt(id);
	    daos.close();
	    b = baos;
	} catch (IOException ex) {
	    Logger.getLogger(MessageMoved.class.getName()).log(Level.SEVERE, null, ex);
	}
	byte[] by = b.toByteArray();
	MessageBundle mb = new MessageBundle();
	mb.bytes = by;
	mb.messageType = getMessageType().ordinal();
	return mb;
    }

    public static MessageMoved getInstance(byte[] b) {
	try (ByteArrayInputStream bais = new ByteArrayInputStream(b); DataInputStream dais = new DataInputStream(bais)) {
	    int x = dais.readInt();
	    int y = dais.readInt();
	    int id = dais.readInt();
	    return new MessageMoved(x, y, id);
	} catch (IOException ex) {
	    Logger.getLogger(MessageMoved.class.getName()).log(Level.SEVERE, null, ex);
	}
	return null;
    }
}
