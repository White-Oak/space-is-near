/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components.inventory;

import java.util.logging.Logger;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentState;
import spaceisnear.game.messages.Message;

public class InventoryComponent extends Component {

    public InventoryComponent() {
	getStates().add(new ComponentState("slots", new TypicalInventorySlotsSet()));
    }

    @Override
    public void processMessage(Message message) {
    }
}