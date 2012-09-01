/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.world.game.components;

import spaceisnear.world.game.messages.Message;
import spaceisnear.world.game.messages.MessageTypes;

public class PlayerControllableComponent extends Component {

    @Override
    public void processMessage(Message message) {
	if (message.getMessageType() == MessageTypes.CONTROLLED) {
	}
    }
}
