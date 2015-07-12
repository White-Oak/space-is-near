package me.whiteoak.minlog;

import java.io.*;

/**
 *
 * @author White Oak
 */
public class FileLogger extends Log.Logger {

    @Override
    public void log(int level, String category, String message, Throwable ex) {
	super.log(level, category, message, ex); //To change body of generated methods, choose Tools | Templates.
	StringBuilder stringBuilder = new StringBuilder();
	stringBuilder.append(getTimeSinceStart());
	stringBuilder.append(getLevel(level));
	stringBuilder.append(message);
	stringBuilder.append(getEx(ex));
	printCategory(category, stringBuilder);
    }

    private void printCategory(String category, CharSequence text) {
	File logsDir = new File("./logs/");
	logsDir.mkdir();
	File file = new File("./logs/" + category + "_log.txt");
	if (file.canWrite()) {
	    try (FileWriter fileWriter = new FileWriter(file)) {
		fileWriter.append(text);
		fileWriter.append(System.lineSeparator());
	    } catch (IOException ex) {
		super.log(Log.LEVEL_ERROR, "minlog", "While writing to " + category + " log file", ex);
	    }
	}
    }
}
