package spaceisnear.game.messages;

import java.io.Serializable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author LPzhelud
 */
@RequiredArgsConstructor public abstract class Message implements Serializable {

    @Getter private final MessageType messageType;

    @Override
    public String toString() {
	return "My type is " + messageType;
    }

}
