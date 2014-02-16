package spaceisnear.game.messages;

import lombok.Getter;

public class MessageLogin extends Message {

    @Getter private final String login, password;

    public MessageLogin(String login, String password) {
	super(MessageType.LOGIN);
	this.login = login;
	this.password = password;
    }

}
