/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import spaceisnear.game.messages.Message;

/**
 *
 * @author LPzhelud
 */
public class IntelligenceComponent extends Component {

    IntelligenceComponent(int owner) {
	super(owner, ComponentType.INTELLIGENCE);
    }

    @Override
    public void processMessage(Message message) {
    }
}
