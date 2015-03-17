/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages.properties;

import com.esotericsoftware.minlog.Logs;
import lombok.Getter;
import spaceisnear.game.GameContext;
import spaceisnear.game.messages.*;

public class MessageYourPlayerDiscovered extends Message implements NetworkableMessage, MessagePropertable {

    @Getter private final int playerID;

    public MessageYourPlayerDiscovered(int playerID) {
	super(MessageType.DISCOVERED_PLAYER);
	this.playerID = playerID;
    }

    @Override
    public void processForClient(GameContext context) {
	context.setNewGamerPlayer(getPlayerID());
	Logs.info("client", "Your player discovered at " + getPlayerID());
	context.getCore().getNetworking().setPlayable(true);
    }

    @Override
    public int getId() {
	return playerID;
    }

}
