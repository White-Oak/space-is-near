package spaceisnear.server.contexteditors;

import java.awt.*;
import java.util.Arrays;
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

    @Override
    public void update(ServerContext context) {
	int[][] temp = context.getAtmosphere().getMap();
	if (pressures == null) {
	    pressures = new int[temp.length][];
	}
	for (int i = 0; i < temp.length; i++) {
	    int[] is = temp[i];
	    pressures[i] = Arrays.copyOf(is, is.length);
	}
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
	JFrame jFrame = new JFrame();
	final Shower shower = new Shower();
	shower.setBackground(Color.black);
	jFrame.setBackground(Color.black);
	jFrame.setSize(600, 600);
	jFrame.getContentPane().setPreferredSize(new Dimension(600, 600));
	jFrame.setResizable(false);
	jFrame.add(shower);
	jFrame.setVisible(true);
    }

    private class Shower extends JPanel {

	int x, y;

	@Override
	protected void paintComponent(Graphics g) {
//	    g.translate(x, y);
	    for (int[] is : pressures) {
		for (int j : is) {
		    g.setColor(getColorFor(j));
		    g.fillRect(0, 0, GameContext.TILE_WIDTH, GameContext.TILE_HEIGHT);
		    g.translate(0, GameContext.TILE_HEIGHT);
		}
		g.translate(GameContext.TILE_WIDTH, 0);
	    }
//	    g.translate(-x, -y);
	}

    }
}
