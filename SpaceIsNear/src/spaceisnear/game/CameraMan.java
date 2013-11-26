/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game;

import lombok.Getter;
import lombok.Setter;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import spaceisnear.game.layer.*;

/**
 *
 * @author LPzhelud
 */
public class CameraMan {

    private int x, y;
    @Getter @Setter private TiledLayer tiledLayer;
    @Setter private int windowWidth, windowHeight;

    @Getter private final ObstaclesLayer obstacles;
    @Getter private final AtmosphericLayer atmosphere;

    public CameraMan() throws SlickException {
	final int width = 128;
	final int height = width;
	tiledLayer = new TiledLayer(new Image(getClass().getResourceAsStream("/res/tiles1.png"), "sprites", false),
		spaceisnear.game.GameContext.TILE_WIDTH,
		spaceisnear.game.GameContext.TILE_HEIGHT, width, height);
	obstacles = new ObstaclesLayer(width, height);
	atmosphere = new AtmosphericLayer(width, height);
    }

    public void delegateWidth() {
	tiledLayer.setWindowHeight(windowHeight);
	tiledLayer.setWindowWidth(windowWidth);
    }

    public void setNewCameraPositionFor(int deltax, int deltay) {
	if (deltax != 0) {
	    if (deltax > 0) {
		cameraRight();
	    } else {
		cameraLeft();
	    }
	} else {
	    if (deltay > 0) {
		cameraDown();
	    } else {
		cameraUp();
	    }
	}
    }

    private void cameraUp() {
	y--;
	tiledLayer.moveUp();
    }

    private void cameraDown() {
	y++;
	tiledLayer.moveDown();
    }

    private void cameraLeft() {
	x--;
	tiledLayer.moveLeft();
    }

    private void cameraRight() {
	x++;
	tiledLayer.moveRight();
    }

    public void moveCamera(Graphics g) {
	g.translate(-x * tiledLayer.getTileWidth(), -y * tiledLayer.getTileWidth());
    }

    public void unmoveCamera(Graphics g) {
	g.translate(-x * tiledLayer.getTileWidth(), -y * tiledLayer.getTileWidth());
    }

    void paint(Graphics g) {
	tiledLayer.paintLayer(g);
    }
}
