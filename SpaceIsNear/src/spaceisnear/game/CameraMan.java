/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game;

import lombok.Getter;
import lombok.Setter;
import org.newdawn.slick.Graphics;
import spaceisnear.game.layer.TiledLayer;

/**
 *
 * @author LPzhelud
 */
public class CameraMan {

    private int x, y;
    @Getter private final TiledLayer tiledLayer;
    private final static int FRAMES_FOR_CAMERA_TO_MOVE = 4;
    @Setter private int windowWidth, windowHeight;

    public CameraMan(TiledLayer tiledLayer) {
	this.tiledLayer = tiledLayer;
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
    }

    private void cameraDown() {
	y++;
    }

    private void cameraLeft() {
	x--;
    }

    private void cameraRight() {
	x++;
    }

    public void moveCamera(Graphics g) {
	g.translate(-x * tiledLayer.getTileWidth(), -y * tiledLayer.getTileWidth());
    }

    public void unmoveCamera(Graphics g) {
	g.translate(-x * tiledLayer.getTileWidth(), -y * tiledLayer.getTileWidth());
    }

    void paint(Graphics g) {
//	//animation
//	x += xdelta;
//	if (xdelta != 0 && Math.abs(x - finalx) < xdelta) {
//	    xdelta = 0;
//	    x = finalx;
//	}
//	y += ydelta;
//	if (ydelta != 0 && Math.abs(y - finaly) < ydelta) {
//	    ydelta = 0;
//	    y = finaly;
//	}
//	//
	tiledLayer.paintLayer(g);
    }
}
