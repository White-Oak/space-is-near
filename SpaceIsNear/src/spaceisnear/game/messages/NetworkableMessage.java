/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

/**
 *
 * @author white_oak
 */
public interface NetworkableMessage {

    public byte[] getBytes();
    public MessageType getMessageType();
}
