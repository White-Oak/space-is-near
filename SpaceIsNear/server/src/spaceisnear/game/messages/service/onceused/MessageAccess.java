package spaceisnear.game.messages.service.onceused;

import lombok.*;
import spaceisnear.game.messages.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE) public class MessageAccess extends Message implements NetworkableMessage {

    @Getter private boolean access;

    public MessageAccess(boolean access) {
	super(MessageType.ACCESS);
	this.access = access;
    }

}
