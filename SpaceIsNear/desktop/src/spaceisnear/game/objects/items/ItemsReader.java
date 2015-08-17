/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects.items;

import spaceisnear.Utils;

/**
 *
 * @author White Oak
 */
public class ItemsReader {

    public static ItemBundle[] read() throws Exception {
	return Utils.getJson("/res/items.json", ItemBundle[].class);
    }

}
