/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects.items;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import spaceisnear.Utils;

/**
 *
 * @author White Oak
 */
public class ItemsReader {

    public static ItemBundle[] read() throws Exception {
	try (InputStream items = ItemsReader.class.getResourceAsStream("/spaceisnear/game/objects/items/items.json")) {
	    byte[] contents = getContents(items);
	    return Utils.GSON.fromJson(new String(contents), ItemBundle[].class);
	} catch (Exception e) {
	    throw e;
	}
    }

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
