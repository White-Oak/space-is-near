/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages.properties;

import lombok.Getter;
import spaceisnear.Utils;
import spaceisnear.game.bundles.MessageBundle;
import spaceisnear.game.messages.DirectedMessage;
import spaceisnear.game.messages.MessageType;

public class MessageNicknameSet extends DirectedMessage implements MessagePropertable {

    @Getter private final String nickname;

    public MessageNicknameSet(int id, String nickname) {
	super(MessageType.NICKNAME_SET, id);
	this.nickname = nickname;
    }

    public static MessageNicknameSet getInstance(byte[] b) {
	return Utils.GSON.fromJson(new String(b), MessageNicknameSet.class);
    }

}
