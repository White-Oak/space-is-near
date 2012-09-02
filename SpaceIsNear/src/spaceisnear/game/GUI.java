/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game;

import java.awt.Graphics;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import lombok.Getter;
import spaceisnear.game.components.PaintableComponent;
import spaceisnear.game.layer.TiledLayer;

/**
 *
 * @author LPzhelud
 */
public final class GUI extends JFrame implements Runnable, ComponentListener {

    private TiledLayer tiledLayer;
    private int startx, starty;
    private boolean clicked;
    private int clickedX, clickedY;
    private final Panel panel;
    private final GameContext context;
    @Getter private int key;

    public GUI(TiledLayer tiledLayer, GameContext context) {
	super("Space Is Near");
	this.tiledLayer = tiledLayer;
	panel = new Panel();
	setLocation(200, 200);
	setSize(800, 600);
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	setContentPane(panel);
	getContentPane().addMouseListener(panel);
	getContentPane().addMouseMotionListener(panel);
	componentResized(null);
	addComponentListener(this);
	this.context = context;
    }

    @Override
    public void run() {
	while (true) {
	    repaint();
	    try {
		Thread.sleep(100L);
	    } catch (Exception e) {
	    }
	}
    }

    private void changeX(int x) {
	startx += x;
	tiledLayer.setX(-startx / tiledLayer.getTileWidth());
    }

    private void changeY(int y) {
	starty += y;
	tiledLayer.setY(-starty / tiledLayer.getTileHeight());
    }

    @Override
    public void componentResized(ComponentEvent e) {
//	tiledLayer.setWindowWidth((int) getSize().getWidth() / tiledLayer.getTileWidth());
//	tiledLayer.setWindowHeight((int) getSize().getHeight() / tiledLayer.getTileHeight());
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    private class Panel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

	@Override
	public void update(Graphics g) {
	    paint(g);
	}

	@Override
	public void paint(Graphics g) {
	    g.translate(startx, starty);
	    tiledLayer.paint(g);
	    for (PaintableComponent paintableComponent : context.getPaintables()) {
		paintableComponent.paint(g);
	    }
	    g.translate(-startx, -starty);
	}

	@Override
	public void paintAll(Graphics g) {
	    paint(g);
	}

	@Override
	protected void paintComponent(Graphics g) {
	    paint(g);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	    clicked = true;
	    clickedX = e.getX();
	    clickedY = e.getY();
//	    System.out.println(clickedX);
//	    System.out.println(clickedY);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	    clicked = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	    if (clicked) {
//		System.out.println("Dragged");
		changeX((e.getX() - clickedX) / 2);
		changeY((e.getY() - clickedY) / 2);
		clickedX = e.getX();
		clickedY = e.getY();
	    }
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent ke) {
	}

	@Override
	public void keyPressed(KeyEvent ke) {
	    key = ke.getKeyCode();
	}

	@Override
	public void keyReleased(KeyEvent ke) {
	}
    }
}
