/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
    private int xdelta, ydelta, finalx, finaly;
    //
    private int lastx, lasty;
    //
    @Setter private int windowWidth, windowHeight;//halfs

    public CameraMan(TiledLayer tiledLayer) {
	this.tiledLayer = tiledLayer;
    }

    public void delegateWidth() {
	tiledLayer.setWindowHeight(windowHeight);
	tiledLayer.setWindowWidth(windowWidth);
    }

    public void setGamerPosition(int x, int y) {
	if (xdelta == 0 && ydelta == 0) {
//	x *= GameContext.TILE_WIDTH;
	    x = x << 4;
//	y *= GameContext.TILE_HEIGHT;
	    y = y << 4;
	    if (lastx == x && lasty == y) {
		return;
	    }
	    int newx = x - (windowWidth >> 1);
	    int newy = y - (windowHeight >> 1);
	    if (newx < 0) {
		newx = 0;
	    }
	    if (newy < 0) {
		newy = 0;
	    }
	    if (newx + windowWidth > tiledLayer.getWidth()) {
		newx = tiledLayer.getWidth() - windowWidth;
	    }
	    if (newy + windowHeight > tiledLayer.getHeight()) {
		newy = tiledLayer.getHeight() - windowHeight;
	    }
	    //
	    xdelta = (newx - this.x) / FRAMES_FOR_CAMERA_TO_MOVE;
	    ydelta = (newy - this.y) / FRAMES_FOR_CAMERA_TO_MOVE;
	    finalx = newx;
	    finaly = newy;
	    tiledLayer.setStartx((finalx >> 4) - 1);
	    tiledLayer.setStarty((finaly >> 4) - 1);
	    lastx = x;
	    lasty = y;
	}

    }

    public void moveCamera(Graphics g) {
	g.translate(-x, -y);
    }

    public void unmoveCamera(Graphics g) {
	g.translate(x, y);
    }

    void paint(Graphics g) {
	//animation
	x += xdelta;
	if (xdelta != 0 && Math.abs(x - finalx) < xdelta) {
	    xdelta = 0;
	    x = finalx;
	}
	y += ydelta;
	if (ydelta != 0 && Math.abs(y - finaly) < ydelta) {
	    ydelta = 0;
	    y = finaly;
	}
	//
	tiledLayer.paintLayer(g);
    }
}
