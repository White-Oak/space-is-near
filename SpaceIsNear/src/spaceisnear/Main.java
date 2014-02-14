/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import spaceisnear.game.Corev2;
import spaceisnear.server.ServerCore;

/**
 *
 * @author LPzhelud
 */
public class Main extends Game {

    public Corev2 core;
    public LoadingScreen screen;
    private Menu menu;
    public static Main main;

    public static void main(String[] args) {
//	args = new String[]{"editor"};
	if (args.length > 0) {
	    switch (args[0]) {
		case "host":
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
		    break;
		case "editor":
		    spaceisnear.editor.Main.main(args);
		    break;
	    }
	} else {
	    LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
	    cfg.title = "Space is Near";
	    cfg.width = 1200;
	    cfg.height = 600;
	    cfg.vSyncEnabled = true;
	    LwjglApplication lwjglApplication = new LwjglApplication(new Main(), cfg);
	}
    }

    @Override
    public void create() {
	main = this;
	menu = new Menu();
	core = new Corev2();
	core.init();
	screen = new LoadingScreen();
	setScreen(menu);
    }

}
