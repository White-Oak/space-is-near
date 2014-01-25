/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import spaceisnear.game.console.LogString;

/**
 *
 * @author White Oak
 */
public class ServerLog {

    private File logFile;
    private FileOutputStream logFOS;

    public ServerLog() {
	try {
	    logFile = new File("log.txt");
	    if (!logFile.exists()) {
		logFile.createNewFile();
	    }
	    logFOS = new FileOutputStream(logFile);
	} catch (FileNotFoundException ex) {
	    Logger.getLogger(ServerLog.class.getName()).log(Level.SEVERE, null, ex);
	} catch (IOException ex) {
	    Logger.getLogger(ServerLog.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    public void log(LogString string) {
	try {
	    Date date = new Date(System.currentTimeMillis());
	    logFOS.write(("[" + date.toString() + "]").getBytes("UTF-8"));
	    logFOS.write((" [" + string.getLevel().toString() + "]: ").getBytes("UTF-8"));
	    logFOS.write(string.getMessage().getBytes("UTF-8"));
	} catch (UnsupportedEncodingException ex) {
	    Logger.getLogger(ServerLog.class.getName()).log(Level.SEVERE, null, ex);
	} catch (IOException ex) {
	    Logger.getLogger(ServerLog.class.getName()).log(Level.SEVERE, null, ex);
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
