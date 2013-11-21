/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear;

import lombok.RequiredArgsConstructor;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import spaceisnear.game.Corev2;

/**
 *
 * @author White Oak
 */
@RequiredArgsConstructor public class LoadingScreen extends BasicGameState {

    private final Corev2 core;

    @Override
    public int getID() {
	return 2;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
	System.out.println("Entered loading screen");
	core.callToConnect();
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
	g.setColor(Color.black);
	g.fillRect(0, 0, game.getContainer().getWidth(), game.getContainer().getHeight());
	g.setColor(Color.yellow);
	g.drawString("Loading...", (game.getContainer().getWidth() >> 1) - (g.getFont().getWidth("Loading...") >> 1),
		(game.getContainer().getHeight() >> 1) - 100);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
	if (!core.isJustConnected()) {
	    System.out.println("Moving to Core...");
	    game.enterState(3, new FadeOutTransition(Color.white, 400), new FadeInTransition(Color.white, 400));
	}
    }
}
