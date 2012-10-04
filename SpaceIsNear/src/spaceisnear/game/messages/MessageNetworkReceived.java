/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import lombok.Getter;

public class MessageNetworkReceived extends Message {

    @Getter private Object object;

    public MessageNetworkReceived(Object obj) {
	super(MessageTypes.NETWORK_RECEIVED);
	this.object = obj;
    }
}
