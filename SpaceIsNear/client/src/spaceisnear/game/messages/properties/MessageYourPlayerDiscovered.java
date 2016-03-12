/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages.properties;

import lombok.*;
import me.whiteoak.minlog.Log;
import spaceisnear.game.GameContext;
import spaceisnear.game.messages.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE) public class MessageYourPlayerDiscovered extends DirectedMessage implements NetworkableMessage, MessagePropertable {

    public MessageYourPlayerDiscovered(int playerID) {
	super(MessageType.DISCOVERED_PLAYER, playerID);
    }

    @Override
    public void processForClient(GameContext context) {
	assert canBeApplied(context);
	context.setNewGamerPlayer(getId());
	Log.info("client", "Your player discovered at " + getId());
	context.getEngine().getNetworking().setPlayable(true);
    }

}
