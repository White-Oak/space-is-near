/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import spaceisnear.game.Corev2;

/**
 *
 * @author LPzhelud
 */
public class Main extends StateBasedGame {

    public Main(String name) {
	super(name);
    }

    public static void main(String[] args) throws SlickException {
	AppGameContainer appGameContainer = new AppGameContainer(new Main("Space is Near"), 800, 600, false);
	appGameContainer.setMinimumLogicUpdateInterval(100);
	appGameContainer.setVSync(true);
	appGameContainer.start();
    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {
//	addState(new Splash());
	addState(new Menu());
	addState(new Corev2());
    }
    
}
