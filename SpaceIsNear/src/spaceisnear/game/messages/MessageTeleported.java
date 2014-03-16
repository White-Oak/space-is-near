/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import spaceisnear.game.GameContext;
import spaceisnear.game.messages.properties.MessagePropertable;
import spaceisnear.game.objects.Position;

public class MessageTeleported extends MessageMoved implements MessagePropertable {

    public MessageTeleported(Position p, int id) {
	super(p, id, 0);
    }

    @Override
    public void processForClient(GameContext context) {
	context.sendDirectedMessage(this);
    }

}
