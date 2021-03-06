package spaceisnear.server.objects.items;

import java.io.*;
import lombok.RequiredArgsConstructor;
import me.whiteoak.minlog.Log;
import spaceisnear.Utils;
import spaceisnear.editor.*;
import spaceisnear.game.objects.items.ItemBundle;
import spaceisnear.game.ui.Position;
import spaceisnear.server.ServerContext;

/**
 *
 * @author White Oak
 */
@RequiredArgsConstructor public class ItemAdder {

    private final ServerContext context;

    public static StaticItem addItem(Position p, int id, ServerContext context, ItemProperty[] propertys) {
	StaticItem item = new StaticItem(context, p, id);
	context.addObject(item);
	//path and air blocking checks
	if (p != null) {
	    final boolean blockingPath = ServerItemsArchive.ITEMS_ARCHIVE.isBlockingPath(id);
	    if (blockingPath) {
		context.getObstacles().setReacheable(p.getX(), p.getY(), false);
	    }
	    final boolean airReachable = !ServerItemsArchive.ITEMS_ARCHIVE.isBlockingAir(id);
	    if (!airReachable) {
		context.getAtmosphere().setAirReacheable(p.getX(), p.getY(), false);
	    }
	}
	final ItemBundle bundle = ServerItemsArchive.ITEMS_ARCHIVE.getBundle(id);
	boolean st = bundle.stuckedByAddingFromScript;
	item.getVariableProperties().setProperty("stucked", st);

	setProperties(bundle, item, propertys);
	return item;
    }

    private static void setProperties(final ItemBundle bundle, StaticItem item, ItemProperty[] propertys) {
	//properties
	if (bundle.defaultProperties != null) {
	    for (ItemBundle.Property property : bundle.defaultProperties) {
		item.getVariableProperties().setProperty(property.getName(), property.getValue());
	    }
	}
	if (propertys != null) {
	    for (ItemProperty property : propertys) {
		item.getVariableProperties().setProperty(property.getName(), property.getValue());
	    }
	}
    }

    public void addItems() throws IOException {
	long time = System.currentTimeMillis();
	SaveLoadAction[] slas = Utils.getJson("/additems.json", SaveLoadAction[].class);
	for (SaveLoadAction sla : slas) {
	    addItem(new Position(sla.getX(), sla.getY()), sla.getItemId(), context, sla.getProperties());
	}
	time = System.currentTimeMillis() - time;
	Log.info("server", "Map was loaded in " + time + " ms.");
    }

}
