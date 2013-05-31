/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import spaceisnear.game.bundles.Bundle;

/**
 *
 * @author white_oak
 */
public interface NetworkableMessage {

    public Bundle getBundle();
    public MessageType getMessageType();
}
