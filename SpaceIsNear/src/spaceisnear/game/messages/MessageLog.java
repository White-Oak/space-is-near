/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import lombok.Getter;
import spaceisnear.game.ui.console.LogString;

public class MessageLog extends Message implements NetworkableMessage {

    @Getter private final LogString log;

    public MessageLog(LogString log) {
	super(MessageType.LOG);
	this.log = log;
    }

}
