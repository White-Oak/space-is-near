/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import spaceisnear.game.messages.propertys.MessagePropertable;
import spaceisnear.game.objects.Position;

public class MessageTeleported extends MessageMoved implements MessagePropertable {

    public MessageTeleported(int x, int y, int id) {
	super(x, y, id, 0);
    }

    public MessageTeleported(Position p, int id) {
	this(p.getX(), p.getY(), id);
    }

    public static MessageTeleported getInstance(byte[] b) {
	try (final ByteArrayInputStream bais = new ByteArrayInputStream(b);
		final DataInputStream dais = new DataInputStream(bais);) {
	    int x = dais.readInt();
	    int y = dais.readInt();
	    int id = dais.readInt();
	    return new MessageTeleported(x, y, id);
	} catch (IOException ex) {
	    Logger.getLogger(MessageMoved.class.getName()).log(Level.SEVERE, null, ex);
	}
	return null;
    }
}
