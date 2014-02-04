/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import spaceisnear.Utils;
import spaceisnear.game.messages.properties.MessagePropertable;
import spaceisnear.game.objects.Position;

public class MessageTeleported extends MessageMoved implements MessagePropertable {

    public MessageTeleported(Position p, int id) {
	super(p, id, 0);
    }

    public static MessageTeleported getInstance(byte[] b) {
	return Utils.GSON.fromJson(new String(b), MessageTeleported.class);
    }
}
