/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import spaceisnear.game.bundles.Bundle;
import spaceisnear.game.bundles.JSONBundle;
import spaceisnear.game.bundles.MessageBundle;
import spaceisnear.game.bundles.NullBundle;
import spaceisnear.game.bundles.ObjectBundle;

/**
 *
 * @author White Oak
 */
public class Registerer {

    private static Class[] classes = {
	Bundle.class,
	MessageBundle.class,
	JSONBundle.class,
	ObjectBundle.class,
	NullBundle.class,
	byte[].class,
	String.class
    };

    public static void registerEverything(Server server) {
	for (int i = 0; i < classes.length; i++) {
	    Class class1 = classes[i];
	    register(class1, server);
	}
    }

    public static void registerEverything(Client client) {
	for (int i = 0; i < classes.length; i++) {
	    Class class1 = classes[i];
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
