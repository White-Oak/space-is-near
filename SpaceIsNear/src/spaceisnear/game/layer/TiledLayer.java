package spaceisnear.game.layer;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.newdawn.slick.Image;

/**
 *
 * @author LPzhelud
 */
public class TiledLayer extends Layer {

    @Getter(AccessLevel.PROTECTED) private final int[][] map;//[x][y]
//    private NullLayer obstaclesMap;
    @Getter private final Image[] tiles;
    @Getter private final int tileWidth, tileHeight;
    @Getter private final int horizontalTilesNumber, verticalTilesNumber;
    private int maxXTiles, maxYTiles;
    private int startx, starty;
    private int startx_absolute, starty_absolute;

    public void setStartx(int startx) {
	this.startx = startx < 0 ? 0 : startx;
    }

    public void setStarty(int starty) {
	this.starty = starty < 0 ? 0 : starty;
    }

    public void moveLeft() {
	startx_absolute--;
	setStartx(startx_absolute);
    }

    public void moveRight() {
	startx_absolute++;
	setStartx(startx_absolute);
    }

    public void moveUp() {
	starty_absolute--;
	setStarty(starty_absolute);
    }

    public void moveDown() {
	starty_absolute++;
	setStarty(starty_absolute);
    }

    public void setWindowWidth(int w) {
	maxXTiles = w / tileWidth + 2;
    }

    public void setWindowHeight(int h) {
	maxYTiles = h / tileHeight + 2;
    }

    public TiledLayer(Image image, int tileWidth, int tileHeight, int width, int height) {
	super(width * tileWidth, height * tileHeight);
	if (image.getWidth() / tileWidth * tileWidth != image.getWidth()
		|| image.getHeight() / tileHeight * tileHeight != image.getHeight()) {
	    throw new IllegalArgumentException();
	}
	this.tileHeight = tileHeight;
	this.tileWidth = tileWidth;
	tiles = chopImage(image);
	map = new int[width][height];
	horizontalTilesNumber = width;
	verticalTilesNumber = height;
    }

    private Image[] chopImage(Image image) {
	int x = 0, y = 0;
	List<Image> list = new ArrayList<>();
	try {
	    while (true) {
		while (true) {
		    Image subImage = image.getSubImage(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
		    list.add(subImage);
		    //x increment
		    x++;
		    if ((x + 1) * tileWidth > image.getWidth()) {
			x = 0;
			break;
		    }
		}
		//y increment
		y++;
		if ((y + 1) * tileHeight > image.getHeight()) {
		    break;
		}
	    }
	} catch (Exception e) {
	    System.out.println(x);
	    System.out.println(y);
	    e.printStackTrace();
	}
	return list.toArray(new Image[list.size()]);
    }

    public void setTile(int x, int y, int tileId) {
	map[x][y] = tileId;
    }

    public int getTile(int x, int y) {
	return map[x][y];
    }

    public void fillRectTile(int x, int y, int w, int h, int tileId) {
	for (int i = y; i < y + h; i++) {
	    for (int j = x; j < w + x; j++) {
		setTile(j, i, tileId);
	    }
	}
    }

    @Override
    public void paintLayer(org.newdawn.slick.Graphics g) {
	int maxw = (startx + maxXTiles) > map.length ? map.length : startx + maxXTiles;
	int maxh = (starty + maxYTiles) > map[0].length ? map[0].length : starty + maxYTiles;
	for (int i = startx; i < maxw; i++) {
	    int[] is = map[i];
	    for (int j = starty; j < maxh; j++) {
		int k = is[j];
		k--;
		if (k != -1) {
		    paintTile(g, i * tileWidth, j * tileHeight, k);
		}
	    }
	}
    }

    protected void paintTile(org.newdawn.slick.Graphics g, int x, int y, int id) {
	g.drawImage(tiles[id], x, y);
    }
}
