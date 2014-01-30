/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import spaceisnear.game.components.client.PaintableComponent;
import org.newdawn.slick.Graphics;
import spaceisnear.game.messages.Message;

public class NameComponent extends PaintableComponent {

    public NameComponent(String name) {
	super(ComponentType.NAME);
	addState(new ComponentState("name", name));
    }

    private NameComponent() {
	super(ComponentType.NAME);
    }

    @Override
    public void processMessage(Message message) {
    }

    @Override
    public void paintComponent(Graphics g) {
    }

    public String getNickname() {
	return (String) getStateValueNamed("name");
    }

    public void setNickname(String name) {
	getStateNamed("name").setValue(name);
    }
}
