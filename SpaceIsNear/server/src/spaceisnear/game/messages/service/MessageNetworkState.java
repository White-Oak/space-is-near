// Generated by delombok at Sun Dec 01 13:28:22 MSK 2013
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages.service;

import lombok.Getter;
import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessageType;

/**
 * @author LPzhelud
 */
public class MessageNetworkState extends Message {

    @Getter private final int state;

//0 - n a
//1 - connected
//2 - disconnected
//3 - idle
    public MessageNetworkState(int state) {
	super(MessageType.NETWORK_STATE);
	this.state = state;
    }

    @Override
    public String toString() {
	switch (state) {
	    case 1:
		return "connected";
	    case 2:
		return "disconnected";
	    case 3:
		return "idle";
	    default:
		return "n/a";
	}
    }

}
