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
import org.apache.commons.cli.*;
import spaceisnear.game.Corev3;
import spaceisnear.server.ServerCore;
import spaceisnear.starting.LoginScreen;

/**
 *
 * @author LPzhelud
 */
public class Main {

    public static String IP;

    public static void main(String[] args) throws ParseException {
	Option mode = new Option("mode", true, "one of default, host, editor");
	Option ip = new Option("hostip", true, "the ip adress of the host");
	Option help = new Option("help", "displays the help");
	Options options = new Options();
	options.addOption(ip);
	options.addOption(mode);
	options.addOption(help);
	CommandLineParser parser = new BasicParser();
	CommandLine parse = parser.parse(options, args);
	String optionValue = parse.getOptionValue("help");
	if (optionValue != null) {
	    HelpFormatter formatter = new HelpFormatter();
	    formatter.printHelp("SIN -mode <mode> -hostip <hostip>", options);
	    return;
	}
	if (parse.hasOption("hostip")) {
	    optionValue = parse.getOptionValue("mode");
	    if (optionValue != null && !optionValue.equals("default")) {
		runSINInWeirdMode(optionValue);
	    } else {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Space is Near";
		cfg.width = 1200;
		cfg.height = 600;
		cfg.vSyncEnabled = true;
		cfg.useGL20 = true;
		IP = parse.getOptionValue("hostip");
		final Corev3 corev3 = new Corev3();
		new LwjglApplication(corev3, cfg);
	    }
	} else {
	    HelpFormatter formatter = new HelpFormatter();
	    formatter.printHelp("SIN mode <mode> hostip <hostip>", options);
	}
    }

    private static void runSINInWeirdMode(String mode) {
	switch (mode) {
	    case "host":
		System.out.println("SIN is running in no-GUI mode");
		try {
		    IP = "127.0.0.1";
		    ServerCore serverCore = new ServerCore();
		    serverCore.host();
		    new Thread(serverCore, "SIN Server").start();
		    System.out.println("Hosted");
		} catch (IOException ex) {
		    Logger.getLogger(LoginScreen.class.getName()).log(Level.SEVERE, null, ex);
		}
		break;
	    case "editor":
		spaceisnear.editor.Main.main(null);
		break;
	}
    }

}
