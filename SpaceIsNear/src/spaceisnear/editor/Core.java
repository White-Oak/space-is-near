/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.editor;

import com.badlogic.gdx.Game;

/**
 *
 * @author White Oak
 */
public class Core extends Game {

    @Override
    public void create() {
	setScreen(new Editor());
    }
}
