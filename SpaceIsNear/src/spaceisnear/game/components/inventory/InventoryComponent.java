/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components.inventory;

import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentType;
import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.properties.MessagePropertySet;

public final class InventoryComponent extends Component {

    public InventoryComponent() {
	super(ComponentType.INVENTORY);
	addState("slots", new TypicalInventorySlotsSet());
    }

    public TypicalInventorySlotsSet getSlots() {
	return (TypicalInventorySlotsSet) getStateValueNamed("slots");
    }

    public void setSlots(TypicalInventorySlotsSet slots) {
	setStateValueNamed("slots", slots);
    }

    @Override
    public void processMessage(Message message) {
	if (message instanceof MessagePropertySet) {
	    getSlots().processMessage(message, getContext());
	}
    }
}
