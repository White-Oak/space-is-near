/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages.service;

import spaceisnear.game.GameContext;
import spaceisnear.game.messages.*;

public class MessagePaused extends Message implements NetworkableMessage {

    public MessagePaused() {
	super(MessageType.PAUSED);
    }

    @Override
    public void processForClient(GameContext context) {
	context.getEngine().pause();
    }

}
