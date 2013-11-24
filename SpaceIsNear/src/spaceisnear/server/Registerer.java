/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import spaceisnear.game.bundles.Bundle;
import spaceisnear.game.bundles.MessageBundle;
import spaceisnear.game.messages.MessageType;

/**
 *
 * @author White Oak
 */
public class Registerer {

    private static final Class[] classes = {
	Bundle.class,
	MessageBundle.class,
	MessageType.class,
	byte[].class,
	String.class
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
