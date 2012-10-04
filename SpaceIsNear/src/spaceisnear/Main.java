/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;
import spaceisnear.game.Corev2;

/**
 *
 * @author LPzhelud
 */
public class Main {

    public static void main(String[] args) throws SlickException {
	Corev2 corev2 = new Corev2("Space is Near");
	AppGameContainer appGameContainer = new AppGameContainer(corev2, 800, 600, false);
	appGameContainer.setMinimumLogicUpdateInterval(100);
	appGameContainer.setVSync(true);
	appGameContainer.start();
    }
}
