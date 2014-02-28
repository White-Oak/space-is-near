package spaceisnear.game.messages;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import spaceisnear.Utils;
import spaceisnear.game.bundles.MessageBundle;

/**
 * @author LPzhelud
 */
@RequiredArgsConstructor public abstract class Message {

    @Getter private final MessageType messageType;

    public MessageBundle getBundle() {
	return new MessageBundle(Utils.GSON.toJson(this).getBytes(), getMessageType());
    }

    public static <T> T createInstance(byte[] b, Class<T> c) {
	return Utils.GSON.fromJson(new String(b), c);
    }
}
