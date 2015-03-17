/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear;

import com.badlogic.gdx.backends.lwjgl.*;
import com.esotericsoftware.minlog.Logs;
import java.io.IOException;
import org.apache.commons.cli.*;
import spaceisnear.game.Corev3;
import spaceisnear.log.Logger;
import spaceisnear.server.*;

/**
 *
 * @author LPzhelud
 */
public class Main {

    public static String IP;
    public static LwjglApplication lwjglApplication;
    public static Logger defaultLogger = new Logger();

    public static void main(String[] args) throws ParseException {
	Logs.setLogger(defaultLogger);
	Options options = prepareOptions();
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
		defaultLogger.client();
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Space is Near";
		cfg.width = 1200;
		cfg.height = 600;
		cfg.vSyncEnabled = true;
		cfg.resizable = false;
		cfg.useGL20 = true;
		IP = parse.getOptionValue("hostip");
		final Corev3 corev3 = new Corev3();
		lwjglApplication = new LwjglApplication(corev3, cfg);
	    }
	} else {
	    HelpFormatter formatter = new HelpFormatter();
	    formatter.printHelp("SIN -mode <mode> -hostip <hostip>", options);
	}
    }

    private static Options prepareOptions() throws IllegalArgumentException {
	Option mode = new Option("mode", true, "one of default, host, editor");
	Option ip = new Option("hostip", true, "the ip adress of the host");
	Option help = new Option("help", "displays the help");
	Options options = new Options();
	options.addOption(ip);
	options.addOption(mode);
	options.addOption(help);
	return options;
    }

    private static void runSINInWeirdMode(String mode) {
	switch (mode) {
	    case "host":
		defaultLogger.chat();
		defaultLogger.server();
		Logs.info("client", "SIN is running in no-GUI mode");
		try {
		    IP = "127.0.0.1";
		    ServerCore serverCore = new ServerCore();
		    serverCore.host();
		    new Thread(serverCore, "SIN Server").start();
		    Logs.info("server", "Hosted");
		} catch (IOException ex) {
		    Logs.error("client", "While trying to start server", ex);
		}
		break;
	    case "editor":
		spaceisnear.editor.Main.main(null);
		break;
	}
    }

}
