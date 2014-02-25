/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects.items;

import java.io.*;
import spaceisnear.Utils;

/**
 *
 * @author White Oak
 */
public class ItemsReader {

    public static ItemBundle[] read() throws Exception {
	try (InputStream items = ItemsReader.class.getResourceAsStream("/spaceisnear/game/objects/items/items.json")) {
	    byte[] contents = Utils.getContents(items);
	    return Utils.GSON.fromJson(new String(contents), ItemBundle[].class);
	} catch (Exception e) {
	    throw e;
	}
    }

}
