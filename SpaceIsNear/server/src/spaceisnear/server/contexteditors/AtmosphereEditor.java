package spaceisnear.server.contexteditors;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import spaceisnear.abstracts.Context;
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
	jFrame.addMouseListener(shower);
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

    private class Shower extends JPanel implements KeyListener, MouseListener {

	int x, y;

	@Override
	protected void paintComponent(Graphics g) {
	    g.setColor(Color.black);
	    g.fillRect(0, 0, getWidth(), getHeight());
	    int xTile = x * Context.TILE_WIDTH;
	    int yTile = y * Context.TILE_HEIGHT;
	    g.translate(-xTile, -yTile);
	    int xAccumulated = 0;
	    for (int[] is : pressures) {
		int yAccumulated = 0;
		for (int j : is) {
		    g.setColor(getColorFor(j));
		    g.fillRect(0, 0, Context.TILE_WIDTH, Context.TILE_HEIGHT);
		    g.translate(0, Context.TILE_HEIGHT);
		    yAccumulated += Context.TILE_HEIGHT;
		}
		g.translate(Context.TILE_WIDTH, -yAccumulated);
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

	@Override
	public void mouseClicked(MouseEvent e) {
	    int x1 = e.getX();
	    int y1 = e.getY();
	    if (x1 > 0 && y1 > 0) {
		int xTile = x * Context.TILE_WIDTH + x1 / Context.TILE_WIDTH;
		int yTile = y * Context.TILE_HEIGHT + y1 / Context.TILE_HEIGHT;
		try {
		    int pressure = pressures[xTile][yTile];
		    String showInputDialog = JOptionPane.showInputDialog("Set the pressure:", pressure);
		    pressures[xTile][yTile] = Integer.parseInt(showInputDialog);
		} catch (ArrayIndexOutOfBoundsException ex) {

		}
	    }
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

    }
}
