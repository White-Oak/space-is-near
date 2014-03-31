// Generated by delombok at Sun Dec 01 13:28:22 MSK 2013
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import lombok.Getter;
import lombok.Setter;
import spaceisnear.game.objects.Position;

/**
 * @author LPzhelud
 */
public final class CameraMan {

    @Getter private int x;
    @Getter private int y;
    @Setter private int windowWidth;
    @Setter private int windowHeight;
    @Getter private final OrthographicCamera camera = new OrthographicCamera();
    @Getter private final OrthographicCamera lightsCamera = new OrthographicCamera();
    @Getter private final int horizontalTilesNumber;
    @Getter private final int verticalTilesNumber;

    public CameraMan() {
	verticalTilesNumber = GameContext.MAP_WIDTH;
	horizontalTilesNumber = verticalTilesNumber;
	camera.setToOrtho(true);
	lightsCamera.setToOrtho(true, 1200f / GameContext.TILE_WIDTH, 600f / GameContext.TILE_HEIGHT);
    }

    public void moveCamera(int deltax, int deltay) {
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

    public void moveCameraTo(int x, int y) {
	this.x = x;
	this.y = y;
//	tiledLayer.moveCameraTo(x, y);
    }

    public void moveCameraToPlayer(int x, int y) {
	moveCameraTo(x - (getMaxXTiles() >> 1), y - (getMaxYTiles() >> 1));
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
    private int savedX, savedY;

    public void moveCamera() {
	savedX = x;
	savedY = y;
	camera.translate(savedX * GameContext.TILE_WIDTH, savedY * GameContext.TILE_HEIGHT);
	camera.update();
	lightsCamera.translate(savedX, savedY);
	lightsCamera.update();
    }

    public void unmoveCamera() {
	camera.translate(-savedX * GameContext.TILE_WIDTH, -savedY * GameContext.TILE_HEIGHT);
	lightsCamera.translate(-savedX, -savedY);
	camera.update();
    }

    public boolean belongsToCamera(Position p) {
	int px = p.getX();
	int py = p.getY();
	if (px < 0 || py < 0) {
	    return false;
	}
//	final boolean xBelongs = px + 1 > this.x + 6 && px - this.x < getMaxXTiles() + 1 - 7;
//	final boolean yBelongs = py + 1 > this.y + 4 && py - this.y < getMaxYTiles() + 1 - 5;
	final boolean xBelongs = px + 2 > this.x && px - this.x < getMaxXTiles() + 1;
	final boolean yBelongs = py + 2 > this.y && py - this.y < getMaxYTiles() + 1;
	return xBelongs && yBelongs;
    }

    public int getMaxXTiles() {
	return windowWidth / GameContext.TILE_WIDTH + 2;
    }

    public int getMaxYTiles() {
	return windowHeight / GameContext.TILE_HEIGHT + 2;
    }

}
