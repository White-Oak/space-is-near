package spaceisnear.server;

import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import spaceisnear.Utils;
import spaceisnear.abstracts.Context;

/**
 *
 * @author White Oak
 */
public class AccountManager {

    private HashMap<String, String> accounts = new HashMap<>();
    private final static Type typeOfT = new TypeToken<HashMap<String, String>>() {
    }.getType();
    private final HashMap<String, Boolean> accessibility = new HashMap<>();

    public AccountManager() {
	File file = new File("accounts.txt");
	try {
	    if (file.exists()) {
		try (FileInputStream fileInputStream = new FileInputStream(file)) {
		    byte[] contents = Utils.getContents(fileInputStream);
		    accounts = Utils.GSON.fromJson(new String(contents), typeOfT);
		}
	    }
	} catch (IOException ex) {
	    Context.LOG.log(ex);
	}
	Thread thread = new Thread("Accounts' thread") {

	    @Override
	    public void run() {
		while (true) {
		    try {
			Thread.sleep(1000 * 30);
		    } catch (InterruptedException ex) {
			Context.LOG.log(ex);
		    }
		    saveAccounts();
		}
	    }
	};
	thread.start();
    }

    public synchronized void saveAccounts() {
	try {
	    synchronized (accounts) {
		final String toJson = Utils.GSON.toJson(accounts, typeOfT);
		File file = new File("accounts.txt");
		if (file.exists()) {
		    file.delete();
		}
		file.createNewFile();
		try (FileOutputStream fos = new FileOutputStream(file, false)) {
		    fos.write(toJson.getBytes());
		}
		Context.LOG.log("Saved accounts' details");
	    }
	} catch (IOException ex) {
	    Context.LOG.log(ex);
	}
    }

    public synchronized boolean isAccessible(String login, String password) {
	synchronized (accounts) {
	    String get = accounts.get(login);
	    if (get == null) {
		accounts.put(login, password);
		return true;
	    } else if (get.equals(password) && !accessibility.containsKey(login)) {
		accessibility.put(login, Boolean.FALSE);
		return true;
	    } else {
		return false;
	    }
	}
    }

}
