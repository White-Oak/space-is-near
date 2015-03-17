package spaceisnear.log;

import com.esotericsoftware.minlog.Logs;
import java.io.*;
import java.util.Date;

/**
 *
 * @author White Oak
 */
public class Logger extends Logs.Logger {

    private File serverLog, clientLog, chatLog;
    private FileOutputStream serverFOS, clientFOS, chatFOS;
    private PrintStream serverPS, clientPS, chatPS;

    public void client() {
	try {
	    clientLog = new File("clientLog.txt");
	    clientFOS = new FileOutputStream(clientLog, true);
	    clientPS = new PrintStream(clientFOS);
	} catch (Exception e) {
	    e.printStackTrace();
	    System.exit(1);
	}
    }

    public void server() {
	try {
	    serverLog = new File("serverLog.txt");
	    serverFOS = new FileOutputStream(serverLog, true);
	    serverPS = new PrintStream(serverFOS);
	} catch (Exception e) {
	    e.printStackTrace();
	    System.exit(1);
	}
    }

    public void chat() {
	try {
	    chatLog = new File("chatLog.txt");
	    chatFOS = new FileOutputStream(chatLog, true);
	    chatPS = new PrintStream(chatFOS);
	} catch (Exception e) {
	    e.printStackTrace();
	    System.exit(1);
	}
    }

    @Override
    public void log(int level, String category, String message, Throwable ex) {
	StringBuilder builder = new StringBuilder(256);
	builder.append('[');
	builder.append(new Date());
	builder.append("]: [");
	builder.append(Logs.getLevelName(level));
	builder.append("] ");
	builder.append(message);
	if (ex != null) {
	    StringWriter writer = new StringWriter(256);
	    ex.printStackTrace(new PrintWriter(writer));
	    builder.append('\n');
	    builder.append(writer.toString().trim());
	}
	System.out.println(builder);
	if (category != null) {
	    switch (category) {
		case "chat":
		    chatPS.append(builder);
		    break;
		case "server":
		    serverPS.append(builder);
		    break;
		case "client":
		    clientPS.append(builder);
		    break;
	    }
	}
    }

    @Override
    protected void finalize() throws Throwable {
	try {
	    if (clientFOS != null) {
		clientFOS.close();
	    }
	    if (serverFOS != null) {
		clientFOS.close();
	    }
	    if (chatFOS != null) {
		clientFOS.close();
	    }
	} finally {
	    super.finalize();
	}
    }

}
