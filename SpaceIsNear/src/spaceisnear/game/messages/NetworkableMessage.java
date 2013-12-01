/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import spaceisnear.game.bundles.MessageBundle;

/**
 *
 * @author white_oak
 */
public interface NetworkableMessage {

    public MessageBundle getBundle();

    public MessageType getMessageType();
}
