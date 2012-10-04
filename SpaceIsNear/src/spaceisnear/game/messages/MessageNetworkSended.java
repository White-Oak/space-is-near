/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import lombok.Getter;

public class MessageNetworkSended extends Message {

    @Getter private byte[] data;

    public MessageNetworkSended(byte[] data) {
	super(MessageTypes.ASKED_TO_SEND);
	this.data = data;
    }
}
