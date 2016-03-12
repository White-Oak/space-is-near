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

    @Getter private int id;

    public DirectedMessage(MessageType messageType, int id) {
	super(messageType);
	this.id = id;
    }

    @Override
    public void processForClient(GameContext context) {
	assert canBeApplied(context);
	context.sendDirectedMessage(this);
    }

    public boolean canBeApplied(GameContext context) {
	return context.getObjects().containsKey(getId());
    }

    @Override
    public void processForServer(ServerContext context, Client client) {
	context.sendDirectedMessage(this);
    }

    @Override
    public boolean isDirected() {
	return true;
    }

}
