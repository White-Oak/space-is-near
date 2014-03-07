package spaceisnear.server;

import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import spaceisnear.Utils;

/**
 *
 * @author White Oak
 */
public class AccountManager {

    private HashMap<String, String> accounts;
    private HashMap<String, Boolean> accountsAccessible = new HashMap<>();
    private final static Type typeOfT = new TypeToken<HashMap<String, String>>() {
    }.getType();

    public AccountManager() {
	File file = new File("accounts.txt");
	try {
	    if (file.exists()) {
		byte[] contents = Utils.getContents(new FileInputStream(file));
		accounts = Utils.GSON.fromJson(new String(contents), typeOfT);
	    } else {
		accounts = new HashMap<>();
		file.createNewFile();
	    }
	} catch (FileNotFoundException ex) {
	    Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
	} catch (IOException ex) {
	    Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    private void saveAccounts() {
	try {
	    final String toJson = Utils.GSON.toJson(accounts, typeOfT);
	    File file = new File("accounts.txt");
	    try (FileOutputStream fos = new FileOutputStream(file, false)) {
		fos.write(toJson.getBytes());
	    }
	} catch (IOException ex) {
	    Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    public boolean isAccessible(String login, String password) {
	String get = accounts.get(login);
	if (get == null) {
	    accounts.put(login, password);
	    saveAccounts();
	    get = password;
	}
	if (get != null && get.equals(password) && accountsAccessible.get(login) == null) {
	    accountsAccessible.put(login, Boolean.FALSE);
	    return true;
	} else {
	    return false;
	}
    }

}
