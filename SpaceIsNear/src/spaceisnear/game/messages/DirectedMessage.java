/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import lombok.*;
import spaceisnear.game.GameContext;
import spaceisnear.server.*;

/**
 *
 * @author White Oak
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED) public abstract class DirectedMessage extends Message {

    @Getter protected int id;

    public DirectedMessage(MessageType messageType, int id) {
	super(messageType);
	this.id = id;
    }

    @Override
    public void processForClient(GameContext context) {
	context.sendDirectedMessage(this);
    }

    @Override
    public void processForServer(ServerContext context, Client client) {
	context.sendDirectedMessage(this);
    }

}
