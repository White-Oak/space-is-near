package spaceisnear.starting;

import java.io.IOException;
import java.util.concurrent.*;
import lombok.RequiredArgsConstructor;
import me.whiteoak.minlog.Log;
import org.apache.commons.cli.ParseException;
import spaceisnear.Main;
import spaceisnear.game.Networking;
import spaceisnear.game.ui.console.*;

/**
 *
 * @author White Oak
 */
@RequiredArgsConstructor public class Starter {

    private final GameConsole console;
    private final Networking networking;

    public Future<Networking> callToConnect(String IP) {
	Callable<Networking> callable = () -> {
	    try {
		try {
		    networking.connect(IP, 54555);
		} catch (IOException ex) {
		    console.pushMessage(new ChatString("Couldn't find a host on " + IP, LogLevel.WARNING));
		    console.pushMessage(new ChatString("Starting server on 127.0.0.1", LogLevel.WARNING));
		    try {
			Main.main(new String[]{"-mode", "host"});
		    } catch (ParseException ex1) {
			Log.error("client", "While trying to start server", ex1);
		    }
		    try {
			networking.connect("127.0.0.1", 54555);
		    } catch (IOException ex1) {
			Log.error("client", "While trying to connect to localhost server", ex1);
			System.exit(1);
		    }
		}
	    } catch (Exception e) {
		Log.error("WTF", e);
	    }
	    return networking;
	};
	return Executors.newSingleThreadExecutor().submit(callable);
    }
}
