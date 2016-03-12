package spaceisnear.game.messages.service.onceused;

import lombok.*;
import spaceisnear.game.messages.*;

/**
 * Sent only when client is connected to server for the first time.
 *
 * @author White Oak
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE) public class MessagePlayerInformation extends Message implements NetworkableMessage {

    @Getter private String desiredNickname, desiredProfession;

    public MessagePlayerInformation(String desiredNickname, String desiredProfession) {
	super(MessageType.PLAYER_INFO);
	this.desiredNickname = desiredNickname;
	this.desiredProfession = desiredProfession;
    }

}
