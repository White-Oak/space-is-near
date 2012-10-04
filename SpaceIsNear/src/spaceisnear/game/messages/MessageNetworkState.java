/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import lombok.Getter;

/**
 *
 * @author LPzhelud
 */
public class MessageNetworkState extends Message {

    @Getter private int state;
//0 - n a
//1 - connected
//2 - disconnected
//3 - idle

    public MessageNetworkState(int state) {
	super(MessageTypes.NETWORK_STATE);
	this.state = state;
    }
}
