/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components.inventory;

import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentType;
import spaceisnear.game.messages.Message;

public class InventoryComponent extends Component {

    public InventoryComponent() {
	super(ComponentType.INVENTORY);
	addState("slots", new TypicalInventorySlotsSet());
    }

    @Override
    public void processMessage(Message message) {
    }
}
