/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components.server;

import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentType;
import spaceisnear.game.messages.Message;

/**
 *
 * @author LPzhelud
 */
public class IntelligenceComponent extends Component {

    IntelligenceComponent() {
	super(ComponentType.INTELLIGENCE);
    }

    @Override
    public void processMessage(Message message) {
    }
}
