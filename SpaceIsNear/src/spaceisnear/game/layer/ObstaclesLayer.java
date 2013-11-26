/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.layer;

import java.util.Arrays;

/**
 *
 * @author White Oak
 */
public class ObstaclesLayer {

    private final boolean[][] obstacles;

    public ObstaclesLayer(int width, int height) {
	obstacles = new boolean[width][height];
	for (boolean[] bs : obstacles) {
	    Arrays.fill(bs, true);
	}
    }

    public void setReacheable(int x, int y, boolean reacheable) {
	obstacles[x][y] = reacheable;
    }

    public boolean isReacheable(int x, int y) {
	try {
	    return obstacles[x][y];
	} catch (Exception e) {
	    return false;
	}
    }
}
