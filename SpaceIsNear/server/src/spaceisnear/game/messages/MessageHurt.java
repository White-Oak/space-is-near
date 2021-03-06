// Generated by delombok at Sun Dec 01 13:28:22 MSK 2013
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import lombok.*;

/**
 * Sent both to client and server if player was hurt. Otherwise \u0432\u0402\u201d sent only to server.
 *
 * @author White Oak
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE) public class MessageHurt extends DirectedMessage implements NetworkableMessage {

    @Getter private int damage;
    @Getter private Type type;

    public MessageHurt(int damage, Type type, int id) {
	super(MessageType.HURT, id);
	this.damage = damage;
	this.type = type;
    }

    public enum Type {

	BLUNT,
	SUFFOCATING,
	BLEEDING,
	RADIOACTIVE,
	TOXIN;

    }
}
