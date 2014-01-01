/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
	new NativeFunction("addItem", 3)
    };
    Constant[] c = {};

    @Override
    public String callNativeFunction(String name, Value[] values) {
	switch (name) {
	    case "addItem":
		int idByName = ItemsArchive.itemsArchive.getIdByName(values[0].getValue());
		Position p = new Position(Integer.valueOf(values[1].getValue()), Integer.valueOf(values[2].getValue()));
		StaticItem staticItem = new StaticItem(context, p, idByName);
		context.addObject(staticItem);
		break;
	}
	return null;
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