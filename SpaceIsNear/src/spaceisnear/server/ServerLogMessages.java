/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.server;

import java.io.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import spaceisnear.game.ui.console.ChatString;

/**
 *
 * @author White Oak
 */
public class ServerLogMessages {

    private File logFile;
    private FileOutputStream logFOS;

    public ServerLogMessages() {
	try {
	    logFile = new File("logMessages.txt");
	    if (!logFile.exists()) {
		logFile.createNewFile();
	    }
	    logFOS = new FileOutputStream(logFile, true);
	} catch (FileNotFoundException ex) {
	    Logger.getLogger(ServerLogMessages.class.getName()).log(Level.SEVERE, null, ex);
	} catch (IOException ex) {
	    Logger.getLogger(ServerLogMessages.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    public void log(ChatString string) {
	try {
	    Date date = new Date(System.currentTimeMillis());
	    logFOS.write(("[" + date.toString() + "]").getBytes("UTF-8"));
	    logFOS.write((" [" + string.getLevel().toString() + "]: ").getBytes("UTF-8"));
	    logFOS.write(string.getMessage().getBytes("UTF-8"));
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
