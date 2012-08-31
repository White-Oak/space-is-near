/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.world.game.components;

import java.util.ArrayList;
import lombok.*;
import spaceisnear.world.game.GameContext;
import spaceisnear.world.game.GameObject;
import spaceisnear.world.game.messages.Message;

/**
 *
 * @author LPzhelud
 */
@RequiredArgsConstructor public abstract class Component {

    @Getter private ComponentState state;
    private final GameContext context;
    @Getter private final GameObject owner;

    public abstract void processMessage(Message message);
}
