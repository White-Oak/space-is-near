/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import spaceisnear.game.Corev3;
import spaceisnear.server.ServerCore;
import spaceisnear.starting.LoginScreen;

/**
 *
 * @author LPzhelud
 */
public class Main {

    public static void main(String[] args) {
	if (args.length > 0) {
	    runSINInWeirdMode(args);
	} else {
	    LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
	    cfg.title = "Space is Near";
	    cfg.width = 1200;
	    cfg.height = 600;
	    cfg.vSyncEnabled = true;
	    cfg.useGL20 = true;
	    final Corev3 corev3 = new Corev3();
	    new LwjglApplication(corev3, cfg);
	}
    }

    private static void runSINInWeirdMode(String[] args) {
	switch (args[0]) {
	    case "host":
		System.out.println("SIN is running in no-GUI mode");
		try {
		    ServerCore serverCore = new ServerCore();
		    serverCore.host();
		    new Thread(serverCore, "SIN Server").start();
		    System.out.println("Hosted");
		} catch (IOException ex) {
		    Logger.getLogger(LoginScreen.class.getName()).log(Level.SEVERE, null, ex);
		}
		break;
	    case "editor":
		spaceisnear.editor.Main.main(args);
		break;
	}
    }

}
