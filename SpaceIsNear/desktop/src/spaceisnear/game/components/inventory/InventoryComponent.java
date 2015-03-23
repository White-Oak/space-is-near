/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components.inventory;

import java.io.Serializable;
import lombok.RequiredArgsConstructor;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentType;
import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.properties.MessageInventoryUpdated;
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
	switch (message.getMessageType()) {
	    case INVENTORY_UPDATE:
		Update update = ((MessageInventoryUpdated) message).update;
		switch (update.type) {
		    case MOVED:
			MovedUpdate mu = (MovedUpdate) update;
			TypicalInventorySlotsSet slots = getSlots();
			synchronized (slots) {
			    slots.add(mu.slotOperated);
			    mu.movedFrom.setItemId(-1);
			    slots.add(mu.movedFrom);
			}
		}
		break;
	}
    }

    @RequiredArgsConstructor public static abstract class Update implements Serializable {

	public final Type type;
	public final InventorySlot slotOperated;

	public static enum Type {

	    MOVED, DROPPED, PICKED_UP
	}
    }

    public static class MovedUpdate extends Update {

	public final InventorySlot movedFrom;

	public MovedUpdate(InventorySlot slotOperated, InventorySlot movedFrom) {
	    super(Type.MOVED, slotOperated);
	    this.movedFrom = movedFrom;
	}

    }
}
