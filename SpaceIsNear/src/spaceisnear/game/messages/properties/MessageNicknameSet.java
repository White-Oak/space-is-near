/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages.properties;

import lombok.Getter;
import spaceisnear.abstracts.AbstractGameObject;
import spaceisnear.game.GameContext;
import spaceisnear.game.messages.DirectedMessage;
import spaceisnear.game.messages.MessageType;
import spaceisnear.game.objects.GameObjectType;
import spaceisnear.game.objects.Player;

public class MessageNicknameSet extends DirectedMessage implements MessagePropertable {

    @Getter private final String nickname;

    public MessageNicknameSet(int id, String nickname) {
	super(MessageType.NICKNAME_SET, id);
	this.nickname = nickname;
    }

    @Override
    public void processForClient(GameContext context) {
	AbstractGameObject get = context.getObjects().get(getId());
	if (get.getType() == GameObjectType.PLAYER || get.getType() == GameObjectType.GAMER_PLAYER) {
	    Player player = (Player) get;
	    player.setNickname(getNickname());
	}
    }

}
