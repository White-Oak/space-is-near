package spaceisnear.server.objects.items;

import java.io.IOException;
import java.util.HashMap;
import lombok.Getter;
import org.whiteoak.parsing.interpretating.ExceptionHandler;
import org.whiteoak.parsing.interpretating.Interpretator;
import org.whiteoak.parsing.interpretating.ast.Constant;
import org.whiteoak.parsing.interpretating.ast.Function;
import spaceisnear.abstracts.Context;
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
    private final InterpretatorBundle[] interpretatorBundles;
    private final HashMap<String, Integer> scriptIds = new HashMap<>();

    public ServerItemsArchive(ItemBundle[] bundles, ItemScriptBundle[] scripts) {
	super(bundles);
	this.scripts = scripts;
	interpretatorBundles = new InterpretatorBundle[scripts.length];
	for (int i = 0; i < scripts.length; i++) {
	    scriptIds.put(scripts[i].name, i);
	}
    }

    private Integer getScriptIdByName(String name) {
	return scriptIds.get(name);
    }

    public Interpretator getInterprator(int id, Constant[] constantses, Function[] functions, ExceptionHandler handler, int mode) {
	final String name = getName(id);
	//Context.LOG.log("Trying to find script for " + name);
	//translate item id to script id
	Integer scriptIdByName = getScriptIdByName(name);
	if (scriptIdByName != null) {
	    id = scriptIdByName;
	    if (scripts[id].hasScript()) {
		Interpretator interpretator = getInterpretatorByMode(mode, id);
		if (interpretator == null) {
		    //Context.LOG.log("Trying to parse script for " + name);
		    interpretator = new Interpretator(constantses, functions, name, handler);
		    try {
			String dir = getDirByMode(mode);
			interpretator.parse(getClass().getResourceAsStream("/res/scripts/" + dir + name + ".script"),
				false);
		    } catch (IOException ex) {
			Context.LOG.log(ex);
		    }
		    setInterpretatorByMode(mode, id, interpretator);
		}
		return interpretator;
	    }
	}
	return null;
    }

    private void setInterpretatorByMode(int mode, int id, Interpretator interpretator) {
	if (interpretatorBundles[id] == null) {
	    interpretatorBundles[id] = new InterpretatorBundle();
	}
	interpretatorBundles[id].interpretators[mode] = interpretator;
    }

    private Interpretator getInterpretatorByMode(int mode, int id) {
	Interpretator interpretator;
	if (interpretatorBundles[id] == null) {
	    return null;
	}
	interpretator = interpretatorBundles[id].interpretators[mode];
	return interpretator;
    }

    private String getDirByMode(int mode) {
	String dir = null;
	switch (mode) {
	    case 0:
		dir = "interaction/";
		break;
	    case 1:
		dir = "messages/";
		break;
	    case 2:
		dir = "time/";
		break;
	    case 3:
		dir = "conreq/";
		break;
	    case 4:
		dir = "conproc/";
		break;
	}
	return dir;
    }

    private class InterpretatorBundle {

	//interaction, messages, time
	Interpretator[] interpretators = new Interpretator[5];

    }

    public spaceisnear.server.objects.items.StaticItem getNewItemForServer(int id, ServerContext serverContext) {
	return new spaceisnear.server.objects.items.StaticItem(serverContext, id);
    }
}
