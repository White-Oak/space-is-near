/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.whiteoak.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author LPzhelud
 */
public class File {

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
}
