/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import spaceisnear.game.Corev2;

/**
 *
 * @author LPzhelud
 */
public class Menu extends BasicGameState implements ComponentListener {

    private int state;
    private boolean hostresult;
    private TextField ip;

    @Override
    public int getID() {
        return 1;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        ip = new TextField(container, game.getContainer().getGraphics().getFont(), 300, 280, 200, 20);
        ip.setMaxLength(15);
        ip.addListener(this);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.setColor(Color.black);
        g.fillRect(0, 0, game.getContainer().getWidth(), game.getContainer().getHeight());
        switch (state) {
            case 0:
                g.setColor(Color.yellow);
                g.drawString("1. host\n2. join", (game.getContainer().getWidth() >> 1) - (g.getFont().getWidth("1. host\n2. join") >> 1), (game.getContainer().getHeight() >> 1) - 100);
                break;
            case 1:
                //не придумал, как покрасивее сделать, поэтому сделал тупо смещение
                g.setColor(Color.yellow);
                g.drawString("1. host\n   2. join\n\nenter ip address:", (game.getContainer().getWidth() >> 1) - (g.getFont().getWidth("1. host\n   2. join\n\nenter ip address:") >> 1), (game.getContainer().getHeight() >> 1) - 100);
                ip.render(container, g);
                break;
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        if (hostresult) {
            Corev2.HOST = true;
            game.enterState(2, new FadeOutTransition(Color.white, 400), new FadeInTransition(Color.white, 400));
        }
        if (state == 2 & !hostresult) {
            Corev2.HOST = false;
            //вырезаем всё, кроме цифр и точек. бизапаснасть
            Corev2.IP = ip.getText().replaceAll("[^0-9^/.]", "");
            game.enterState(2, new FadeOutTransition(Color.white, 400), new FadeInTransition(Color.white, 400));
            System.out.println("connecting to " + Corev2.IP + "...");
        }

    }
    //Хз, что хотел реализовать
    //я тоже не знаю

    @Override
    public void keyPressed(int key, char c) {
        switch (state) {
            case 0:
                switch (c) {
                    case '1':
                        hostresult = true;
                        break;
                    case '2':
                        state++;
                        break;
                }
                break;
            case 1:
                break;
        }
    }

    @Override
    public void componentActivated(AbstractComponent ac) {
        if (ac.equals(ip)) {
            state = 2;
            hostresult = false;
        }
    }
}
