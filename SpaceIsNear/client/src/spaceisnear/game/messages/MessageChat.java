/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import lombok.*;
import spaceisnear.game.ui.console.ChatString;

@NoArgsConstructor(access = AccessLevel.PRIVATE) public class MessageChat extends Message implements NetworkableMessage {

    @Getter private ChatString log;

    public MessageChat(ChatString log) {
	super(MessageType.LOG);
	this.log = log;
    }

}
