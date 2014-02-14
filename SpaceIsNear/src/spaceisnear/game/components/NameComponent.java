/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import spaceisnear.game.components.client.PaintableComponent;
import spaceisnear.game.messages.Message;

public class NameComponent extends PaintableComponent {

    public NameComponent(String name) {
	super(ComponentType.NAME);
	addState("name", name);
    }

    public NameComponent() {
	super(ComponentType.NAME);
    }

    @Override
    public void processMessage(Message message) {
    }

    public String getNickname() {
	return (String) getStateValueNamed("name");
    }

    public void setNickname(String name) {
	setStateValueNamed("name", name);
    }

    @Override
    public void paintComponent(SpriteBatch batch, int x, int y) {
    }
}
