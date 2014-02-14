/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.ui.context;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 *
 * @author White Oak
 */
public class ContextMenu extends Actor {

    private final ContextSubMenu subMenu;

    public ContextMenu(int x, int y) {
	BitmapFont font = new BitmapFont(Gdx.files.classpath("default.fnt"), true);
	subMenu = new ContextSubMenu(null, x, y, font);
    }

    public void render(SpriteBatch batch) {
	batch.end();
	subMenu.render(batch);
	batch.begin();
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
	render(batch);
    }

    public boolean add(ContextMenuItem e) {
	return subMenu.add(e);
    }

    public boolean add(String str) {
	return add(new ContextMenuItem(str));
    }

    public void mouseMoved(int x, int y) {
	subMenu.mouseMoved(x, y);
    }

    public void mouseClicked(int button, int x, int y, int clickCount) {
	subMenu.mouseClicked(button, x, y, clickCount);
    }
}
