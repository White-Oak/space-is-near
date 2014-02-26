/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.messages;

import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import spaceisnear.Utils;
import spaceisnear.game.bundles.MessageBundle;
import spaceisnear.game.components.inventory.InventorySlot;
import spaceisnear.game.components.inventory.TypicalInventorySlotsSet;
import spaceisnear.game.messages.properties.MessagePropertable;

public class MessageInventorySet extends DirectedMessage implements MessagePropertable {

    @Getter private final HashMap<String, InventorySlot> slots;

    private final static Type typeOfT = new TypeToken<HashMap<String, InventorySlot>>() {
    }.getType();

    public MessageInventorySet(int id, TypicalInventorySlotsSet set) {
	super(MessageType.INVENTORY_SET, id);
	slots = set.getSlots();
    }

    private MessageInventorySet(HashMap<String, InventorySlot> map, int id) {
	super(MessageType.INVENTORY_SET, id);
	slots = map;
    }

    @Override
    public MessageBundle getBundle() {
	try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); DataOutputStream dos = new DataOutputStream(baos)) {
	    dos.writeUTF(Utils.GSON.toJson(slots, typeOfT));
	    dos.writeInt(id);
	    return new MessageBundle(baos.toByteArray(), getMessageType());
	} catch (IOException ex) {
	    Logger.getLogger(MessageInventorySet.class.getName()).log(Level.SEVERE, null, ex);
	}
	return null;
    }

    public static MessageInventorySet getInstance(byte[] b) {
	try (ByteArrayInputStream bais = new ByteArrayInputStream(b); DataInputStream dis = new DataInputStream(bais)) {
	    String hash = dis.readUTF();
	    int id = dis.readInt();
	    return new MessageInventorySet((HashMap<String, InventorySlot>) Utils.GSON.fromJson(hash, typeOfT), id);
	} catch (IOException ex) {
	    Logger.getLogger(MessageInventorySet.class.getName()).log(Level.SEVERE, null, ex);
	}
	return null;
    }

}
