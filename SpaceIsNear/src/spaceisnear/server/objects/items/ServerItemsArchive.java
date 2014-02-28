package spaceisnear.server.objects.items;

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

    public ServerItemsArchive(ItemBundle[] bundles, ItemScriptBundle[] scripts) {
	super(bundles);
	this.scripts = scripts;
	interpretators = new Interpretator[scripts.length];
    }

    public Interpretator getInterprator(int id, Constant[] constantses, Function[] functions, ExceptionHandler handler) {
	if (interpretators[id] == null && scripts[id].hasScript()) {
	    interpretators[id] = new Interpretator(constantses, functions, getName(id), handler);
	    interpretators[id].parse("spaceisnear\\server\\objects\\items\\scripts\\" + getName(id) + ".script", false);
	}
	return interpretators[id];
    }

    public spaceisnear.server.objects.items.StaticItem getNewItemForServer(int id, ServerContext serverContext) {
	return new spaceisnear.server.objects.items.StaticItem(serverContext, id);
    }
}
