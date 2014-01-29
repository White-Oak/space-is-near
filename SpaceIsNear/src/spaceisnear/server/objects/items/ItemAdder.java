package spaceisnear.server.objects.items;

import java.io.IOException;
import org.whiteoak.parsing.interpretating.ExceptionHandler;
import org.whiteoak.parsing.interpretating.IAcceptable;
import org.whiteoak.parsing.interpretating.Interpretator;
import org.whiteoak.parsing.interpretating.ast.Constant;
import org.whiteoak.parsing.interpretating.ast.Function;
import org.whiteoak.parsing.interpretating.ast.NativeFunction;
import org.whiteoak.parsing.interpretating.ast.Value;
import spaceisnear.game.objects.Position;
import spaceisnear.game.objects.items.ItemsArchive;
import spaceisnear.server.ServerContext;

/**
 *
 * @author White Oak
 */
public class ItemAdder implements IAcceptable, ExceptionHandler {

    private final ServerContext context;
    Function[] f = {
	new NativeFunction("addItem", 3),
	new NativeFunction("fillWithItem", 5)
    };
    Constant[] c = {};

    @Override
    public String callNativeFunction(String name, Value[] values) {
	switch (name) {
	    case "addItem":
		int idByName = ItemsArchive.itemsArchive.getIdByName(values[0].getValue());
		Position p = new Position(Integer.valueOf(values[1].getValue()), Integer.valueOf(values[2].getValue()));
		addItem(p, idByName);
		break;
	    case "fillWithItem":
		int idByName1 = ItemsArchive.itemsArchive.getIdByName(values[0].getValue());
		int x = Integer.valueOf(values[1].getValue());
		int y = Integer.valueOf(values[2].getValue());
		int endX = Integer.valueOf(values[3].getValue());
		int endY = Integer.valueOf(values[4].getValue());
		for (int i = x; i <= endX; i++) {
		    for (int j = y; j <= endY; j++) {
			Position p1 = new Position(i, j);
			addItem(p1, idByName1);
		    }
		}
	}
	return null;
    }

    private void addItem(Position p, int id) {
	StaticItem staticItem1 = new StaticItem(context, p, id);
	context.addObject(staticItem1);
	context.getObstacles().setReacheable(p.getX(), p.getY(), !ItemsArchive.itemsArchive.isBlockingPath(id));
	context.getAtmosphere().setAirReacheable(p.getX(), p.getY(), !ItemsArchive.itemsArchive.isBlockingAir(id));
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
