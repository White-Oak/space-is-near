/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects.items;

import java.io.InputStream;
import java.io.InputStreamReader;
import spaceisnear.Utils;

/**
 *
 * @author White Oak
 */
public class ItemsReader {

    public static ItemBundle[] read() throws Exception {
	InputStream is = ItemsReader.class.getResourceAsStream("/res/items.json");
	InputStreamReader isr = new InputStreamReader(is);
	return Utils.GSON.fromJson(isr, ItemBundle[].class);
    }

}
