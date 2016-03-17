package spaceisnear;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.GdxNativesLoader;
import me.whiteoak.minlog.FileLogger;
import me.whiteoak.minlog.Log;
import org.apache.commons.cli.*;
import spaceisnear.game.Engine;
import spaceisnear.starting.LoginScreen;
import spaceisnear.starting.ui.Corev3;

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
	String optionValue;
	if (hasOption) {
	    printHelp(options);
	} else {
	    optionValue = parse.getOptionValue("mode");
	    if (optionValue != null && !optionValue.equals("default")) {
		if (parse.hasOption("mode")) {
		    runSINInWeirdMode(optionValue);
		} else {
		    printHelp(options);
		}
	    } else {
		if (parse.hasOption("hostip")) {
		    IP = parse.getOptionValue("hostip");
		} else {
		    IP = "127.0.0.1";
		}
		runSIN();
	    }
	}
    }

    private static void runSIN() {
	GdxNativesLoader.load();
	Log.info("client", "SIN is starting. Version: " + VersionCode.getCode());
	LwjglApplicationConfiguration cfg = configureApp();
	final Corev3 corev3 = new Corev3();
	final Engine engine = new Engine(corev3);
	final LoginScreen loginScreen = new LoginScreen();
	corev3.setStartingScreen(loginScreen);
	corev3.setUpdatable(engine);
	lwjglApplication = new LwjglApplication(corev3, cfg);
    }

    private static void testVozvratKaretki() {
	for (int i = 0; i < 100; i++) {
	    System.out.print("Loading: " + i + "% ");

	    try {
		Thread.sleep(200L);
	    } catch (InterruptedException ex) {
	    }
	    System.out.print("\r");

	}
    }

    private static void printHelp(Options options) {
	HelpFormatter formatter = new HelpFormatter();
	formatter.printHelp("SIN -mode <mode> -hostip <hostip>", options);
    }

    private static LwjglApplicationConfiguration configureApp() {
	LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
	cfg.title = "Space is Near";
	cfg.width = 1200;
	cfg.height = 600;
	cfg.vSyncEnabled = true;
	cfg.resizable = false;
//	cfg.backgroundFPS = -1;
//	cfg.foregroundFPS = 100;
	cfg.samples = 8;
	return cfg;
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

    private static void runSINInWeirdMode(String mode) {
	switch (mode) {
	    case "editor":
		spaceisnear.editor.Main.main(null);
		break;
	    default:
		throw new RuntimeException();
	}
    }

}
