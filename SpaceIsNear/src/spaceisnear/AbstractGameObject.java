/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear;

import java.util.List;
import spaceisnear.game.GameContext;
import spaceisnear.game.components.Component;
import spaceisnear.game.messages.Message;

/**
 *
 * @author White Oak
 */
public abstract class AbstractGameObject {

    public abstract void message(Message m);

    public abstract List<Component> getComponents();

    public abstract void process();

    public abstract Object getBundle();

    public abstract Context getContext();
}
