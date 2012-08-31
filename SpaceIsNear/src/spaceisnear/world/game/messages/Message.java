/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.world.game.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author LPzhelud
 */
@AllArgsConstructor public abstract class Message {

    @Getter private MessageTypes messageType;
}
