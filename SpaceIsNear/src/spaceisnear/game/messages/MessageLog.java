/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import spaceisnear.Utils;
import spaceisnear.game.bundles.MessageBundle;
import spaceisnear.game.ui.console.LogString;

public class MessageLog extends Message implements NetworkableMessage {

    @Getter private final LogString log;

    public MessageLog(LogString log) {
	super(MessageType.LOG);
	this.log = log;
    }

    @Override
    public MessageBundle getBundle() {
	MessageBundle messageBundle = new MessageBundle(getMessageType());
	try {
	    messageBundle.bytes = Utils.GSON.toJson(log).getBytes("UTF-8");
	} catch (UnsupportedEncodingException ex) {
	    Logger.getLogger(MessageLog.class.getName()).log(Level.SEVERE, null, ex);
	}
	return messageBundle;
    }

    public static MessageLog getInstance(byte[] b) {
	try {
	    LogString log = Utils.GSON.fromJson(new String(b, "UTF-8"), LogString.class);
	    return new MessageLog(log);
	} catch (UnsupportedEncodingException ex) {
	    Logger.getLogger(MessageLog.class.getName()).log(Level.SEVERE, null, ex);
	}
	return null;
    }

}
