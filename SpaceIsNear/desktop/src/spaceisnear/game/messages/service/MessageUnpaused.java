/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages.service;

import spaceisnear.game.GameContext;
import spaceisnear.game.messages.*;

/**
 *
 * @author White Oak
 */
public class MessageUnpaused extends Message implements NetworkableMessage {

    public MessageUnpaused() {
	super(MessageType.UNPAUSED);
    }

    @Override
    public void processForClient(GameContext context) {
	context.getEngine().unpause();
    }
}
