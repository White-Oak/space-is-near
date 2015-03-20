package spaceisnear.game.messages.service.onceused;

import lombok.*;
import spaceisnear.game.messages.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE) public class MessageLogin extends Message implements NetworkableMessage {

    @Getter private String login, password;

    public MessageLogin(String login, String password) {
	super(MessageType.CLIENT_INFO);
	this.login = login;
	this.password = password;
    }

}
