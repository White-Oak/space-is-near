package spaceisnear.server.contexteditors;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import spaceisnear.game.GameContext;
import spaceisnear.server.ServerContext;

/**
 *
 * @author White Oak
 */
public class AtmosphereEditor extends ContextEditor implements InterfaceShowable {

    private int[][] pressures;
    private JFrame jFrame;

    @Override
    public void update(ServerContext context) {
	if (pressures == null) {
	    pressures = context.getAtmosphere().getMap();
	}
//	int[][] temp = context.getAtmosphere().getMap();
//	if (pressures == null) {
//	    pressures = new int[temp.length][];
//	}
//	for (int i = 0; i < temp.length; i++) {
//	    int[] is = temp[i];
//	    pressures[i] = Arrays.copyOf(is, is.length);
//	}
    }

    private static Color getColorFor(int pressure) {
	if (pressure == -1) {
	    return Color.darkGray;
	}
	if (pressure == 100) {
	    return Color.GREEN;
	}
	float green = pressure / 100f;
	return new Color(1 - green, green, 0);
    }

    @Override
    public void show() {
	jFrame = new JFrame();
	final Shower shower = new Shower();
	shower.setBackground(Color.black);
	jFrame.addKeyListener(shower);
	jFrame.setBackground(Color.black);
	jFrame.setTitle("Atmosphere Editor");
	jFrame.setSize(600, 600);
	jFrame.getContentPane().setPreferredSize(new Dimension(600, 600));
	jFrame.setResizable(false);
	jFrame.add(shower);
	jFrame.setVisible(true);
    }

    @Override
    public void repaint() {
	jFrame.repaint();
    }

    @Override
    public boolean isShown() {
	return jFrame != null && jFrame.isVisible();
    }

    private class Shower extends JPanel implements KeyListener {

	int x, y;

	@Override
	protected void paintComponent(Graphics g) {
	    g.setColor(Color.black);
	    g.fillRect(0, 0, getWidth(), getHeight());
	    int xTile = x * GameContext.TILE_WIDTH;
	    int yTile = y * GameContext.TILE_HEIGHT;
	    g.translate(-xTile, -yTile);
	    int xAccumulated = 0;
	    for (int[] is : pressures) {
		int yAccumulated = 0;
		for (int j : is) {
		    g.setColor(getColorFor(j));
		    g.fillRect(0, 0, GameContext.TILE_WIDTH, GameContext.TILE_HEIGHT);
		    g.translate(0, GameContext.TILE_HEIGHT);
		    yAccumulated += GameContext.TILE_HEIGHT;
		}
		g.translate(GameContext.TILE_WIDTH, -yAccumulated);
	    }
	    g.translate(xTile - xAccumulated, yTile);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	    switch (e.getKeyCode()) {
		case KeyEvent.VK_DOWN:
		    y++;
		    break;
		case KeyEvent.VK_UP:
		    y--;
		    break;
		case KeyEvent.VK_LEFT:
		    x--;
		    break;
		case KeyEvent.VK_RIGHT:
		    x++;
		    break;
	    }
	    repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

    }
}
