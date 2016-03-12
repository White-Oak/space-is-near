package spaceisnear.game.messages;

import java.io.Serializable;
import lombok.*;
import spaceisnear.game.GameContext;

/**
 * @author LPzhelud
 */
@RequiredArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED) public abstract class Message implements Serializable {

    @Getter @NonNull private MessageType messageType;

    @Override
    public String toString() {
	return "My type is " + messageType;
    }

    public void processForClient(GameContext context) {

    }

    public boolean isDirected() {
	return false;
    }

}
