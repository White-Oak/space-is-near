/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import org.newdawn.slick.*;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.StateBasedGame;
import spaceisnear.game.Corev2;
import spaceisnear.game.ui.console.GameConsole;
import spaceisnear.server.ServerCore;

/**
 *
 * @author LPzhelud
 */
public class Main extends StateBasedGame {

    public Main(String name) {
	super(name);
    }

    public static void main(String[] args) throws SlickException {
	if (args.length > 0) {
	    if (args[0].equals("host")) {
		System.out.println("SIN is running in no-GUI mode");
		try {
		    System.out.println("Hosting...");
		    ServerCore serverCore = new ServerCore();
		    try {
			serverCore.host();
		    } catch (IOException ex) {
			Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
		    }
		    new Thread(serverCore).start();
		    System.out.println("Hosted");
		} catch (IOException ex) {
		    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
		}
	    }
	} else {
	    AppGameContainer appGameContainer = new AppGameContainer(new Main("Space is Near"), 1200, 600, false);
	    appGameContainer.setMinimumLogicUpdateInterval(100);
	    appGameContainer.setVSync(true);
	    appGameContainer.setUpdateOnlyWhenVisible(false);
	    appGameContainer.setSmoothDeltas(true);
//	    appGameContainer.setTargetFrameRate(60);
	    appGameContainer.start();
//	    UnicodeFont font = getFont();
//	    appGameContainer.setDefaultFont(font);
	}
    }

    private static UnicodeFont getFont() {
	final java.awt.Font awtFont = new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 16);
	final UnicodeFont font = new UnicodeFont(awtFont);
	font.getEffects().add(new ColorEffect(java.awt.Color.black));
	font.getEffects().add(new ColorEffect(java.awt.Color.lightGray));
	font.getEffects().add(new ColorEffect(java.awt.Color.gray));
	font.getEffects().add(new ColorEffect(java.awt.Color.white));
	font.addGlyphs(0x0400, 0x04FF);
	font.addAsciiGlyphs();
	try {
	    font.loadGlyphs();
	} catch (SlickException ex) {
	    Logger.getLogger(GameConsole.class.getName()).log(Level.SEVERE, null, ex);
	}
	return font;
    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {
	//	addState(new Splash());
	Corev2 corev2 = new Corev2();
	addState(new Menu());
	addState(new LoadingScreen(corev2));
	addState(corev2);
    }
}
