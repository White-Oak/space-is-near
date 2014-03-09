/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages.properties;

import lombok.Getter;
import spaceisnear.game.messages.*;

public class MessagePropertySet extends DirectedMessage implements NetworkableMessage, MessagePropertable {

    @Getter private final String name;
    @Getter private final Object value;
    private final String valueClass;

    public MessagePropertySet(int id, String name, Object value) {
	super(MessageType.PROPERTY_SET, id);
	this.name = name;
	this.value = value;
	valueClass = value.getClass().getName();
    }

    public MessagePropertySet(MessageType type, int id, String name, Object value) {
	super(type, id);
	this.name = name;
	this.value = value;
	valueClass = value.getClass().getName();
    }

}
