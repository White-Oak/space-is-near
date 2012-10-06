/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import spaceisnear.game.Corev2;

/**
 *
 * @author LPzhelud
 */
public class Menu extends BasicGameState {

    private int state;
    private StringBuilder input = new StringBuilder();
    private boolean hostresult;

    @Override
    public int getID() {
	return 1;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
	g.setColor(Color.black);
	g.fillRect(0, 0, game.getContainer().getWidth(), game.getContainer().getHeight());
	switch (state) {
	    case 0:
		g.setColor(Color.yellow);
		g.drawString("1. host \n2. connect", game.getContainer().getWidth() >> 1, game.getContainer().getHeight() >> 1);
		break;
	    case 1:

	}
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
	if (state == 1) {
	    if (hostresult) {
		Corev2.HOST = true;
		game.enterState(2, new FadeOutTransition(Color.white, 400), new FadeInTransition(Color.white, 400));
	    } else {
		Corev2.HOST = false;
		Corev2.IP = "127.0.0.1";
		game.enterState(2, new FadeOutTransition(Color.white, 400), new FadeInTransition(Color.white, 400));
	    }
	}
    }
    //Хз, что хотел реализовать

    @Override
    public void keyPressed(int key, char c) {
	switch (state) {
	    case 0:
		switch (c) {
		    case '1':
			hostresult = true;
		    case '2':
			state++;
			break;
		}
		break;
	    case 1:
	}
    }
}
