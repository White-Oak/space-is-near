/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.editor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.whiteoak.parsing.interpretating.*;
import org.whiteoak.parsing.interpretating.ast.*;

/**
 *
 * @author White Oak
 */
public class Loader implements IAcceptable, ExceptionHandler {

    Function[] f = {
	new NativeFunction("addItem", 3),
	new NativeFunction("fillWithItem", 5),
	new NativeFunction("addItem", 4),
	new NativeFunction("fillWithItem", 6)
    };
    Constant[] c = {};
    private final ItemsHandler handler = ItemsHandler.HANDLER;
    @Getter private final List<Item> items = new ArrayList<>();

    @Override
    public String callNativeFunction(String name, Value[] values) {
	int rotate = 0;
	switch (name) {
	    case "addItem": {
//		if (values.length == 4) {
//		    rotate = Integer.parseInt(values[3].getValue());
//		}
		int idByName = handler.getIdByName(values[0].getValue());
//		Position p = new Position(Integer.valueOf(values[1].getValue()), Integer.valueOf(values[2].getValue()));
		Item item = new Item(idByName, Integer.valueOf(values[1].getValue()), Integer.valueOf(values[2].getValue()));
//		StaticItem addItem = addItem(p, idByName, context);
//		addItem.getVariableProperties().setProperty("rotate", rotate);
		items.add(item);
	    }
	    break;
	    case "fillWithItem": {
//		if (values.length == 6) {
//		    rotate = Integer.parseInt(values[3].getValue());
//		}
		int idByName1 = handler.getIdByName(values[0].getValue());
		int x = Integer.valueOf(values[1].getValue());
		int y = Integer.valueOf(values[2].getValue());
		int endX = Integer.valueOf(values[3].getValue());
		int endY = Integer.valueOf(values[4].getValue());
		for (int i = x; i <= endX; i++) {
		    for (int j = y; j <= endY; j++) {
//			Position p1 = new Position(i, j);
//			StaticItem addItem1 = addItem(p1, idByName1, context);
//			addItem1.getVariableProperties().setProperty("rotate", rotate);
			Item item = new Item(idByName1, i, j);
			items.add(item);
		    }
		}
	    }
	    break;
	}
	return null;
    }

    public void addItems(InputStream read) throws IOException {
	Interpretator interpretator = new Interpretator(c, f, "additems", this);
	interpretator.parse(read, true);
	interpretator.run(this, true);
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
