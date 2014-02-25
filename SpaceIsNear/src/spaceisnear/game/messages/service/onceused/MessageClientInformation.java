package spaceisnear.game.messages.service.onceused;

import lombok.Getter;
import spaceisnear.game.messages.*;

public class MessageClientInformation extends Message implements NetworkableMessage {

    @Getter private final String login, password;

    public MessageClientInformation(String login, String password) {
	super(MessageType.CLIENT_INFO);
	this.login = login;
	this.password = password;
    }

}
