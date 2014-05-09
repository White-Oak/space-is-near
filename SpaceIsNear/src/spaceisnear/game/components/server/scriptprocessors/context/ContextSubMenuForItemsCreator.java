package spaceisnear.game.components.server.scriptprocessors.context;

import java.util.ArrayList;
import lombok.Getter;
import org.whiteoak.parsing.interpretating.*;
import org.whiteoak.parsing.interpretating.ast.*;
import spaceisnear.server.ServerContext;
import spaceisnear.server.objects.items.ServerItemsArchive;
import spaceisnear.server.objects.items.StaticItem;

/**
 *
 * @author White Oak
 */
class ContextSubMenuForItemsCreator implements IAcceptable, ExceptionHandler {

//    @Getter private ContextMenu subMenu;
    @Getter private final ArrayList<String> subMenu = new ArrayList<>();
    @Getter private int defaults;
    private final static Function[] fs = {
	new NativeFunction("getProperty", 1),
	new NativeFunction("addDefaultActions"),
	new NativeFunction("add", 1)};
    private final StaticItem item;
    private final Interpretator interpretator;

    public ContextSubMenuForItemsCreator(StaticItem item, ServerContext context) {
	this.item = item;

	interpretator = ServerItemsArchive.ITEMS_ARCHIVE.getInterprator(item.getProperties().getId(), new Constant[]{}, fs,
		this, 3);
    }

    public void run() {
	subMenu.clear();
	if (interpretator != null) {
	    interpretator.run(this, false);
	    System.out.println("Created submenu!");
	} else {
	    addDefaultActions();
	}
    }

    @Override
    public String callNativeFunction(String name, Value[] values) {
	switch (name) {
	    case "getProperty":
		return getProperty(values[0].getValue()).toString();
	    case "addDefaultActions":
		addDefaultActions();
		break;
	    case "add":
		subMenu.add(values[0].getValue());
		break;

	}
	return null;
    }

    private void addDefaultActions() {
	subMenu.add("Learn");
	subMenu.add("Pull");
	subMenu.add("Take");
	defaults = 3;
    }

    public Object getProperty(String name) {
	final Object property = item.getVariablePropertiesComponent().getProperty(name);
	if (property == null) {
	    return "null";
	}
	return property;
    }

    @Override
    public void paused() {
    }

    @Override
    public void acceptException(Exception excptn) {
    }
}
