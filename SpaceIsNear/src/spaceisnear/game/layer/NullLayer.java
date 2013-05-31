/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.layer;

/**
 * <p>
 * Obstacles' system.
 * <p/>
 * @author white_oak
 */
public class NullLayer {

    private boolean[][] map;//[x][y]

    public boolean checkTile(int x, int y) {
	return map[x][y];
    }

    public void setPassable(int x, int y, boolean passability) {
	map[x][y] = passability;
    }
}
