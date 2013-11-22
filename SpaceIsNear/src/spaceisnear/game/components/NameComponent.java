/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import org.newdawn.slick.Graphics;
import spaceisnear.game.messages.Message;

public class NameComponent extends PaintableComponent {

    NameComponent() {
    }

    public NameComponent(PositionComponent positionComponent, String name) {
	super(positionComponent);
	getStates().add(new ComponentState("name", name));
    }

    @Override
    public void processMessage(Message message) {
    }

    @Override
    public void paintComponent(Graphics g) {
    }

    public String getNickname() {
	return (String) getStates().get(1).getValue();
    }

    public void setNickname(String name) {
	getStates().get(1).setValue(name);
    }
}
