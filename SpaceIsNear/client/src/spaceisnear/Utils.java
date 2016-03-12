/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear;

import com.google.gson.Gson;
import java.io.*;

/**
 * Just utils.
 *
 * @author White Oak
 */
public class Utils {

    /**
     * Replacement for all the 'new Gson()' lines.
     */
    public static final Gson GSON = new Gson();

    public static byte[] getContents(InputStream is) {
	try {
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    int c;
	    while ((c = is.read()) != -1) {
		baos.write(c);
	    }
	    return baos.toByteArray();
	} catch (IOException ex) {
	    ex.printStackTrace();
	}
	return null;
    }

    public static <E> E getJson(String resourcePath, Class<E> type) {
	InputStream is = Utils.class.getResourceAsStream(resourcePath);
	InputStreamReader isr = new InputStreamReader(is);
	return Utils.GSON.fromJson(isr, type);
    }
}
