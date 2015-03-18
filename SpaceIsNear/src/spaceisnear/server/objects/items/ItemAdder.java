package spaceisnear.server.objects.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.io.File;
import java.io.IOException;
import spaceisnear.Utils;
import spaceisnear.editor.*;
import spaceisnear.game.objects.Position;
import spaceisnear.game.objects.items.ItemBundle;
import spaceisnear.server.ServerContext;

/**
 *
 * @author White Oak
 */
public class ItemAdder {

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
	return item;
    }

    public ItemAdder(ServerContext context) {
	this.context = context;
    }

    public void addItems() throws IOException {
	FileHandle fh = Gdx.files.local("additems.json");
	File f = fh.file();
	if (f.exists()) {
	    {
		SaveLoadAction[] slas = Utils.GSON.fromJson(fh.reader(), SaveLoadAction[].class);
		for (SaveLoadAction sla : slas) {
		    addItem(new Position(sla.getX(), sla.getY()), sla.getItemId(), context, sla.getProperties());
		}
	    }
	}
    }

}
