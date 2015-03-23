/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import spaceisnear.Utils;

/**
 *
 * @author White Oak
 */
public class ItemsReader {

    public static ItemBundle[] read() throws Exception {
	FileHandle internal = Gdx.files.internal("res/items.json");
	return Utils.GSON.fromJson(internal.reader(), ItemBundle[].class);
    }

}
