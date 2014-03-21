package spaceisnear.server.contexteditors;

import java.util.ArrayList;
import spaceisnear.server.ServerContext;

/**
 *
 * @author White Oak
 */
public class ContextEditorsManager {

    private final ArrayList<ContextEditor> editors = new ArrayList<>();
    private final ArrayList<InterfaceShowable> paintables = new ArrayList<>();

    public boolean addEditor(ContextEditor e) {
	return editors.add(e);
    }

    public boolean addPaintable(InterfaceShowable e) {
	return paintables.add(e);
    }

    private void repaint() {
	paintables.forEach(paintable -> paintable.repaint());
    }

    private void update(ServerContext context) {
	editors.forEach(editor -> editor.update(context));
    }

    private void makeChanges(ServerContext context) {
	editors.forEach(editor -> editor.makeChangesTo(context));
    }
}
