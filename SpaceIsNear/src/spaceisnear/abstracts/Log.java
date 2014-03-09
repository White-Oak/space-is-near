package spaceisnear.abstracts;

import java.io.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import spaceisnear.server.*;

/**
 *
 * @author White Oak
 */
public abstract class Log {

    private File logFile;
    private FileOutputStream logFOS;
    private PrintStream logPS;

    protected Log(String fileName) {
	try {
	    logFile = new File(fileName);
	    if (!logFile.exists()) {
		logFile.createNewFile();
	    }
	    logFOS = new FileOutputStream(logFile, true);
	    logPS = new PrintStream(logFOS);
	} catch (FileNotFoundException ex) {
	    Logger.getLogger(ServerLogMessages.class.getName()).log(Level.SEVERE, null, ex);
	} catch (IOException ex) {
	    Logger.getLogger(ServerLogMessages.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    public void log(int string) {
	log(Integer.toString(string));
    }

    public void log(Object string) {
	log(string.toString());
    }

    public void log(String string) {
	System.out.println(string);
	try {
	    Date date = new Date(System.currentTimeMillis());
	    logFOS.write(("[" + date.toString() + "]:").getBytes("UTF-8"));
	    logFOS.write(string.getBytes("UTF-8"));
	    logFOS.write(System.lineSeparator().getBytes("UTF-8"));
	} catch (UnsupportedEncodingException ex) {
	    Logger.getLogger(ServerLogMessages.class.getName()).log(Level.SEVERE, null, ex);
	} catch (IOException ex) {
	    Logger.getLogger(ServerLogMessages.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    public void log(Exception exception) {
	exception.printStackTrace();
	try {
	    Date date = new Date(System.currentTimeMillis());
	    logFOS.write(("[" + date.toString() + "]: ").getBytes("UTF-8"));
	    exception.printStackTrace(logPS);
	    logFOS.write(System.lineSeparator().getBytes("UTF-8"));
	} catch (UnsupportedEncodingException ex) {
	    Logger.getLogger(ServerLogMessages.class.getName()).log(Level.SEVERE, null, ex);
	} catch (IOException ex) {
	    Logger.getLogger(ServerLogMessages.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    @Override
    protected void finalize() throws Throwable {
	try {
	    logFOS.close();
	} finally {
	    super.finalize();
	}
    }
}
