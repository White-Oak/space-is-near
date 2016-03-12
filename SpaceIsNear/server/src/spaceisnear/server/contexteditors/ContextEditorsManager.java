package spaceisnear.server.contexteditors;

import me.whiteoak.minlog.Log;
import java.util.ArrayList;
import spaceisnear.server.ServerContext;

/**
 *
 * @author White Oak
 */
public class ContextEditorsManager {

    private final ArrayList<ContextEditor> editors = new ArrayList<>();
    private final ArrayList<InterfaceShowable> paintables = new ArrayList<>();
    private final ArrayList<ConsoleShowable> printables = new ArrayList<>();

    public void addDefaultCase() {
	addEditor(new AtmosphereEditor());
    }

    public boolean addEditor(ContextEditor e) {
	if (e instanceof InterfaceShowable) {
	    addPaintable((InterfaceShowable) e);
	}
	return editors.add(e);
    }

    private boolean addPaintable(InterfaceShowable e) {
	return paintables.add(e);
    }

    public void repaint() {
	paintables.stream()
		.filter(paintable -> !paintable.isShown())
		.forEach(paintable -> paintable.show());
	paintables.forEach(paintable -> paintable.repaint());
	printables.forEach(printable -> printable.print());
    }

    public void update(ServerContext context) {
	editors.forEach(editor -> editor.update(context));
    }

    public void makeChanges(ServerContext context) {
	editors.forEach(editor -> editor.makeChangesTo(context));
    }

    public void startUpdateCycle(ServerContext context) {
	Runnable r = () -> {
	    updateCycle(context);
	};
	new Thread(r, "Context Editors Manager's update cycle").start();
    }

    private void updateCycle(ServerContext context) {
	while (true) {
	    update(context);
	    makeChanges(context);
	    repaint();
	    try {
		Thread.sleep(1000L);
	    } catch (InterruptedException ex) {
		Log.error("server", "While trying to sleep in ContextEditorManager", ex);
	    }
	}
    }
}
