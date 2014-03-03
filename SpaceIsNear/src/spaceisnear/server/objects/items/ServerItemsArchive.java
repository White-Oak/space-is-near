package spaceisnear.server.objects.items;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import org.whiteoak.parsing.interpretating.ExceptionHandler;
import org.whiteoak.parsing.interpretating.Interpretator;
import org.whiteoak.parsing.interpretating.ast.Constant;
import org.whiteoak.parsing.interpretating.ast.Function;
import spaceisnear.abstracts.ItemsArchivable;
import spaceisnear.game.objects.items.*;
import spaceisnear.server.ServerContext;
import spaceisnear.server.objects.items.scripts.ItemScriptBundle;

/**
 *
 * @author White Oak
 */
public class ServerItemsArchive extends ItemsArchivable {

    public static ServerItemsArchive ITEMS_ARCHIVE;
    @Getter private final ItemScriptBundle[] scripts;
    private final Interpretator[] interpretators;
    private final HashMap<String, Integer> scriptIds = new HashMap<>();

    public ServerItemsArchive(ItemBundle[] bundles, ItemScriptBundle[] scripts) {
	super(bundles);
	this.scripts = scripts;
	interpretators = new Interpretator[scripts.length];
	for (int i = 0; i < scripts.length; i++) {
	    scriptIds.put(scripts[i].name, i);
	}
    }

    private Integer getScriptIdByName(String name) {
	return scriptIds.get(name);
    }

    public Interpretator getInterprator(int id, Constant[] constantses, Function[] functions, ExceptionHandler handler) {
	final String name = getName(id);
	System.out.println("Trying to find script for " + name);
	//translate item id to script id
	Integer scriptIdByName = getScriptIdByName(name);
	if (scriptIdByName != null) {
	    id = scriptIdByName;
	    if (scripts[id].hasScript()) {
		System.out.println("Trying to parse script for " + name);
		interpretators[id] = new Interpretator(constantses, functions, name, handler);
		try {
		    interpretators[id].parse(getClass().getResourceAsStream("/res/scripts/" + name + ".script"),
			    false);
		} catch (IOException ex) {
		    Logger.getLogger(ServerItemsArchive.class.getName()).log(Level.SEVERE, null, ex);
		}
		return interpretators[id];
	    }
	}
	return null;
    }

    public spaceisnear.server.objects.items.StaticItem getNewItemForServer(int id, ServerContext serverContext) {
	return new spaceisnear.server.objects.items.StaticItem(serverContext, id);
    }
}
