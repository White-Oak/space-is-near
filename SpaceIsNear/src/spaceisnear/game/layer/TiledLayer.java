package spaceisnear.game.layer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;

/**
 *
 * @author LPzhelud
 */
public class TiledLayer extends Layer {

    @Getter(AccessLevel.PROTECTED) private final int[][] map;//[x][y]
    @Getter private final BufferedImage[] tiles;
    @Getter private final int tileWidth, tileHeight;
    @Getter private final int horizontalTilesNumber, verticalTilesNumber;

    public TiledLayer(BufferedImage image, int tileWidth, int tileHeight, int width, int height) {
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

    private BufferedImage[] chopImage(BufferedImage image) {
	int x = 0, y = 0;
	List<BufferedImage> list = new ArrayList<>();
	try {
	    while (true) {
		while (true) {
		    BufferedImage subImage = image.getSubimage(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
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
	return list.toArray(new BufferedImage[list.size()]);
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
    public void paint(Graphics g) {
	for (int i = 0; i < map.length; i++) {
	    int[] is = map[i];
	    for (int j = 0; j < is.length; j++) {
		int k = is[j];
		k--;
		if (k != -1) {
		    paintTile(g, i * tileWidth, j * tileHeight, k);
		}
	    }
	}
    }

    protected void paintTile(Graphics g, int x, int y, int id) {
	g.drawImage(tiles[id], x, y, null);
    }
}
