package spaceisnear.game.messages.properties;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import spaceisnear.game.components.inventory.InventoryComponent.Update;
import spaceisnear.game.messages.*;
import spaceisnear.server.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE) public class MessageInventoryUpdated extends DirectedMessage implements MessagePropertable {

    public Update update;

    public MessageInventoryUpdated(int id, Update update) {
	super(MessageType.INVENTORY_UPDATE, id);
	this.update = update;
    }

    @Override
    public void processForServer(ServerContext context, Client client) {
	context.sendDirectedMessage(new MessageToSend(this));
    }

}
