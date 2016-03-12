package spaceisnear;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import java.io.IOException;
import me.whiteoak.minlog.FileLogger;
import me.whiteoak.minlog.Log;
import org.apache.commons.cli.*;
import spaceisnear.server.ServerCore;

/**
 *
 * @author LPzhelud
 */
public class Main {

    public static String IP;
    public static LwjglApplication lwjglApplication;

    public static void main(String[] args) throws ParseException {
	Log.setLogger(new FileLogger());
	Log.DEBUG();
	Options options = prepareOptions();
	CommandLineParser parser = new BasicParser();
	CommandLine parse = parser.parse(options, args);
	boolean hasOption = parse.hasOption("help");
	if (hasOption) {
	    printHelp(options);
	} else {
	    if (parse.hasOption("hostip")) {
		IP = parse.getOptionValue("hostip");
	    } else {
		IP = "127.0.0.1";
	    }
	    Log.info("server", "SIN is starting. Version: " + VersionCode.getCode());
	    try {
		ServerCore serverCore = new ServerCore();
		serverCore.host();
		new Thread(serverCore, "SIN Server").start();
		Log.info("server", "Server started");
	    } catch (IOException ex) {
		Log.error("server", "While trying to start server", ex);
	    }
	}
    }

    private static void printHelp(Options options) {
	HelpFormatter formatter = new HelpFormatter();
	formatter.printHelp("SIN-server -hostip <hostip>", options);
    }

    private static Options prepareOptions() throws IllegalArgumentException {
	Option mode = new Option("mode", true, "one of [default, host, editor]");
	Option ip = new Option("hostip", true, "the ip address of the host");
	Option help = new Option("help", "displays the help");
	Options options = new Options();
	options.addOption(ip);
	options.addOption(mode);
	options.addOption(help);
	return options;
    }

}
