/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import lombok.Getter;
import spaceisnear.game.GameContext;
import spaceisnear.game.ui.console.ChatString;
import spaceisnear.server.*;

public class MessageLog extends Message implements NetworkableMessage {

    @Getter private final ChatString log;

    public MessageLog(ChatString log) {
	super(MessageType.LOG);
	this.log = log;
    }

    @Override
    public void processForServer(ServerContext context, Client client) {
	context.chatLog(log);
    }

    @Override
    public void processForClient(GameContext context) {
	context.getCore().chat(getLog());
    }

}
