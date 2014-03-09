package spaceisnear.game.messages;

import java.io.Serializable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author LPzhelud
 */
@RequiredArgsConstructor public abstract class Message implements Serializable {

    @Getter private final MessageType messageType;
}
