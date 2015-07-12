/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import java.util.HashMap;
import spaceisnear.game.components.inventory.InventoryComponent;
import spaceisnear.game.components.inventory.InventorySlot;
import spaceisnear.game.components.server.context.ServerContextMenu;
import spaceisnear.game.components.server.context.ServerContextSubMenu;
import spaceisnear.game.messages.*;
import spaceisnear.game.messages.properties.*;
import spaceisnear.game.messages.service.*;
import spaceisnear.game.messages.service.onceused.*;
import spaceisnear.game.objects.GameObjectType;
import spaceisnear.game.ui.Position;
import spaceisnear.game.objects.items.Size;
import spaceisnear.game.ui.console.ChatString;
import spaceisnear.game.ui.console.LogLevel;

/**
 *
 * @author White Oak
 */
public class Registerer {

    private static final Class[] classes = {
	int[].class,
	String.class,
	String[].class,
	Integer.class,
	Message.class,
	NetworkableMessage.class,
	DirectedMessage.class,
	MessageHurt.class,
	MessageHurt.Type.class,
	MessageActionChosen.class,
	MessageActionsOffer.class,
	ServerContextMenu.class,
	ServerContextSubMenu.class,
	ServerContextSubMenu[].class,
	MessageActionsRequest.class,
	MessageAnimationChanged.class,
	MessageChat.class,
	ChatString.class,
	Position.class,
	LogLevel.class,
	MessageControlledByInput.class,
	MessageControlledByInput.Type.class,
	MessageCreated.class,
	GameObjectType.class,
	MessageCreatedItem.class,
	MessageDied.class,
	MessageInteracted.class,
	MessageInventorySet.class,
	HashMap.class,
	MessageKnockbacked.class,
	MessageLogin.class,
	MessagePositionChanged.class,
	MessageType.class,
	MessageInventoryUpdated.class,
	InventoryComponent.Update.class,
	InventoryComponent.Update.Type.class,
	Size.class,
	InventorySlot.class,
	MessageNicknameSet.class,
	MessagePropertable.class,
	MessagePropertySet.class,
	MessageYourPlayerDiscovered.class,
	MessageConnectionBroken.class,
	MessagePaused.class,
	MessageRogerRequested.class,
	MessageRogered.class,
	MessageUnpaused.class,
	MessageAccess.class,
	MessageLogin.class,
	MessageError.class,
	MessageGeneralInformation.class,
	MessageJoined.class,
	MessageNewPlayerRequested.class,
	MessagePlayerInformation.class,
	MessageWorldInformation.class
    };

    public static void registerEverything(Server server) {
	for (Class class1 : classes) {
	    register(class1, server);
	}
    }

    public static void registerEverything(Client client) {
	for (Class class1 : classes) {
	    register(class1, client);
	}
    }

    private static void register(Class classs, Server server) {
	Kryo kryo = server.getKryo();
	kryo.register(classs);
    }

    private static void register(Class classs, Client client) {
	Kryo kryo = client.getKryo();
	kryo.register(classs);
    }
}
