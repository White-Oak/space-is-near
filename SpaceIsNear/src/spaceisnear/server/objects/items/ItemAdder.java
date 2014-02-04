package spaceisnear.server.objects.items;

import java.io.IOException;
import org.whiteoak.parsing.interpretating.*;
import org.whiteoak.parsing.interpretating.ast.*;
import spaceisnear.game.objects.Position;
import spaceisnear.game.objects.items.ItemBundle;
import spaceisnear.server.ServerContext;

/**
 *
 * @author White Oak
 */
public class ItemAdder implements IAcceptable, ExceptionHandler {

    private final ServerContext context;
    Function[] f = {
	new NativeFunction("addItem", 3),
	new NativeFunction("fillWithItem", 5),
	new NativeFunction("addItem", 4),
	new NativeFunction("fillWithItem", 6)
    };
    Constant[] c = {};

    @Override
    public String callNativeFunction(String name, Value[] values) {
	int rotate = 0;
	switch (name) {
	    case "addItem":
		if (values.length == 4) {
		    rotate = Integer.parseInt(values[3].getValue());
		}
		int idByName = ServerItemsArchive.itemsArchive.getIdByName(values[0].getValue());
		Position p = new Position(Integer.valueOf(values[1].getValue()), Integer.valueOf(values[2].getValue()));
		StaticItem addItem = addItem(p, idByName, context);
		addItem.getVariableProperties().setProperty("rotate", rotate);
		break;
	    case "fillWithItem":
		if (values.length == 6) {
		    rotate = Integer.parseInt(values[3].getValue());
		}
		int idByName1 = ServerItemsArchive.itemsArchive.getIdByName(values[0].getValue());
		int x = Integer.valueOf(values[1].getValue());
		int y = Integer.valueOf(values[2].getValue());
		int endX = Integer.valueOf(values[3].getValue());
		int endY = Integer.valueOf(values[4].getValue());
		for (int i = x; i <= endX; i++) {
		    for (int j = y; j <= endY; j++) {
			Position p1 = new Position(i, j);
			StaticItem addItem1 = addItem(p1, idByName1, context);
			addItem1.getVariableProperties().setProperty("rotate", rotate);
		    }
		}
	}
	return null;
    }

    public static StaticItem addItem(Position p, int id, ServerContext context) {
	StaticItem staticItem1 = new StaticItem(context, p, id);
	context.addObject(staticItem1);
	if (p != null) {
	    final boolean blockingPath = ServerItemsArchive.itemsArchive.isBlockingPath(id);
	    if (blockingPath) {
		context.getObstacles().setReacheable(p.getX(), p.getY(), false);
	    }
	    context.getAtmosphere().setAirReacheable(p.getX(), p.getY(), !ServerItemsArchive.itemsArchive.isBlockingAir(id));
	}
	final ItemBundle bundle = ServerItemsArchive.itemsArchive.getBundle(id);
	boolean st = bundle.stuckedByAddingFromScript;
	staticItem1.getVariableProperties().setProperty("stucked", st);
	//properties
	if (bundle.defaultProperties != null) {
	    for (ItemBundle.Property property : bundle.defaultProperties) {
		staticItem1.getVariableProperties().setProperty(property.getName(), property.getValue());
	    }
	}
	return staticItem1;
    }

    public ItemAdder(ServerContext context) {
	this.context = context;
    }

    public void addItems() throws IOException {
	Interpretator interpretator = new Interpretator(c, f, "additems", this);
	interpretator.parse(getClass().getResourceAsStream("/spaceisnear/server/objects/items/additems"), false);
	interpretator.run(this, false);
    }

    @Override
    public void paused() {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void acceptException(Exception ex) {
	ex.printStackTrace();
    }

}
