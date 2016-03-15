// Generated by delombok at Sun Dec 01 13:28:22 MSK 2013
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.OrthographicCamera;
import lombok.Getter;
import lombok.Setter;
import spaceisnear.game.ui.Position;

/**
 * @author LPzhelud
 */
public final class CameraMan {

    @Getter private int x;
    @Getter private int y;
    private int actualX, actualY;
    @Setter private int windowWidth;
    @Setter private int windowHeight;
    @Getter private final int horizontalTilesNumber;
    @Getter private final int verticalTilesNumber;
    private final static int CAMERA_ANIMATION_FACTOR = 3;

    public CameraMan() {
	verticalTilesNumber = GameContext.MAP_WIDTH;
	horizontalTilesNumber = verticalTilesNumber;
    }

    public void animate() {
	int xx = x * GameContext.TILE_WIDTH - actualX;
	if (Math.abs(xx) <= 1) {
	    actualX = x * GameContext.TILE_WIDTH;
	} else {
	    int dx = (xx) / CAMERA_ANIMATION_FACTOR;
	    actualX += dx;
	}
	final int yy = y * GameContext.TILE_HEIGHT - actualY;
	if (Math.abs(yy) <= 1) {
	    actualY = y * GameContext.TILE_HEIGHT;
	} else {
	    int dy = (yy) / CAMERA_ANIMATION_FACTOR;
	    actualY += dy;
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

    private int savedActualX, savedActualY;

    public void moveCamera(OrthographicCamera camera) {
	savedActualX = (int) actualX;
	savedActualY = (int) actualY;
	camera.translate(savedActualX, savedActualY);
	camera.update();
    }

    public void unmoveCamera(OrthographicCamera camera) {
	camera.translate(-savedActualX, -savedActualY);
//	lightsCamera.translate(-savedX, -savedY);
	camera.update();
    }

    public boolean belongsToCamera(Position p, Engine engine) {
	int px = p.getX();
	int py = p.getY();
//	final boolean xBelongs = px + 1 > this.x + 6 && px - this.x < getMaxXTiles() + 1 - 7;
//	final boolean yBelongs = py + 1 > this.y + 4 && py - this.y < getMaxYTiles() + 1 - 5;
	final boolean xBelongs = px + 2 > this.x && px - this.x < getMaxXTiles() + 1;
	final boolean yBelongs = py + 2 > this.y && py - this.y < getMaxYTiles() + 1;
	if (!(xBelongs && yBelongs)) {
	    return false;
	}
	float xx = (px + 0.5f);
	float yy = (py + 0.5f);
	PointLight playerLight = engine.getCore().getPointLight();
	Position position = engine.getContext().getPlayer().getPosition();
	return playerLight
		//if exactly that position is in view
		.contains(px * GameContext.TILE_WIDTH, py * GameContext.TILE_HEIGHT)
		|| playerLight
		//if position slightly more 
		.contains((xx + Math.signum(position.getX() - px)) * GameContext.TILE_WIDTH,
			yy * GameContext.TILE_HEIGHT)
		|| playerLight
		.contains(xx * GameContext.TILE_WIDTH,
			(yy + Math.signum(position.getY() - py)) * GameContext.TILE_HEIGHT)
		|| playerLight
		.contains((xx + Math.signum(position.getX() - px)) * GameContext.TILE_WIDTH,
			(yy + Math.signum(position.getY() - py)) * GameContext.TILE_HEIGHT);
    }

    public int getMaxXTiles() {
	return windowWidth / GameContext.TILE_WIDTH + 2;
    }

    public int getMaxYTiles() {
	return windowHeight / GameContext.TILE_HEIGHT + 2;
    }

}
