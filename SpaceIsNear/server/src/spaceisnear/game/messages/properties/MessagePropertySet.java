/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages.properties;

import lombok.*;
import spaceisnear.game.messages.*;
import spaceisnear.server.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE) public class MessagePropertySet extends DirectedMessage implements NetworkableMessage, MessagePropertable {
    
    @Getter private String name;
    @Getter private Object value;
    
    public MessagePropertySet(int id, String name, Object value) {
	this(MessageType.PROPERTY_SET, id, name, value);
    }
    
    public MessagePropertySet(MessageType type, int id, String name, Object value) {
	super(type, id);
	this.name = name;
	this.value = value;
    }
    
    @Override
    public void processForServer(ServerContext context, Client client) {
//	Context.LOG.log(getName() + " " + getValue().getClass().getName());
	context.sendDirectedMessage(this);
    }
    
}
