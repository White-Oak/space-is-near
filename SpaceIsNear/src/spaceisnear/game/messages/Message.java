/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import spaceisnear.game.bundles.MessageBundle;

/**
 *
 * @author LPzhelud
 */
@AllArgsConstructor public abstract class Message {

    @Getter private final MessageType messageType;

    public MessageBundle getBundle() {
	return new MessageBundle(getMessageType());
    }
}